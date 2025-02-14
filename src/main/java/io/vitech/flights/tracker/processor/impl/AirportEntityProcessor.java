package io.vitech.flights.tracker.processor.impl;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.CityEntity;
import io.vitech.flights.tracker.entity.TimezoneEntity;
import io.vitech.flights.tracker.helper.ChunkProcessor;
import io.vitech.flights.tracker.helper.ResponseParser;
import io.vitech.flights.tracker.mapper.AirportMapper;
import io.vitech.flights.tracker.openai.OpenAIService;
import io.vitech.flights.tracker.openai.model.AirportPromptModel;
import io.vitech.flights.tracker.processor.BaseProcessor;
import io.vitech.flights.tracker.repository.AirportRepository;
import io.vitech.flights.tracker.repository.CityRepository;
import io.vitech.flights.tracker.repository.TimezoneRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "vitech.tracker.processor.airport-entity.enabled", havingValue = "true")
public class AirportEntityProcessor extends BaseProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(AirportEntityProcessor.class);

    @Value("${vitech.tracker.processor.airport-entity.chunk-size}")
    private int chunkSize;

    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;
    private final TimezoneRepository timezoneRepository;
    private final ResponseParser responseParser;
    private final AirportMapper airportMapper;

    public AirportEntityProcessor(final OpenAIService openAIService, final AirportRepository airportRepository, final CityRepository cityRepository, final TimezoneRepository  timezoneRepository,
                                  final ResponseParser responseParser, final AirportMapper airportMapper) {
        super(openAIService);
        this.airportRepository = airportRepository;
        this.cityRepository = cityRepository;
        this.timezoneRepository = timezoneRepository;
        this.responseParser = responseParser;
        this.airportMapper = airportMapper;
    }

    @Override
    public void process() {
        LOGGER.debug(PROCESSING_START_LOG_MSG_TEMPLATE, this.getClass().getSimpleName());

        final Set<AirportPromptModel> airportPromptModels = new HashSet<>();

        airportPromptModels.addAll(airportRepository.findAllWithNullLatitudeOrLongitude().stream()
                                                                                         .map(f -> airportMapper.toDto(f))
                                                                                         .collect(Collectors.toSet()));

        if(airportPromptModels.isEmpty()) {
            LOGGER.debug("No flights airports to process.");
            return;
        }

        List<Set<AirportPromptModel>> chunks = ChunkProcessor.chunkify(airportPromptModels, chunkSize);

        try {
            for (Set<AirportPromptModel> chunk : chunks) {
                LOGGER.debug("Processing chunk: {}", chunk);
                // Process each chunk
                String structuredResponse = openAIService.getStructuredResponse(chunk);
                LOGGER.debug("StructuredResponse = {}", structuredResponse);

                if (StringUtils.isEmpty(structuredResponse)) {
                    LOGGER.debug("No airports to process..");
                    continue;
                }

                List<AirportPromptModel> airports = responseParser.parseResponse(structuredResponse, AirportPromptModel.class);
                airports.forEach(airportModel -> {
                    final AirportEntity a = airportMapper.toEntity(airportModel);
                    populateCity(a,airportModel);
                    LOGGER.debug("Update airport with {} ", a);
                    airportRepository.updateAirportDetailsByIataCode(a.getName(), a.getLatitude(), a.getLongitude(), a.getCity().getId(), a.getIataCode());
                });
            }

        } catch (Exception e) {
            LOGGER.error("Error processing airports", e);
            throw new RuntimeException(e);
        }
        LOGGER.debug(PROCESSING_END_MSG_TEMPLATE, this.getClass().getSimpleName());
    }

    @Transactional
    private void populateCity(AirportEntity airportEntity, AirportPromptModel airportPromptModel) {
        if (airportPromptModel.getCityName() != null) {
            LOGGER.info("City name is not null");

            Optional<CityEntity> cityEntityOptional = cityRepository.findByNameAndTimezone(
                    airportPromptModel.getCityName(), airportPromptModel.getTimezone()
            );

            CityEntity cityEntity = cityEntityOptional.orElseGet(() -> {
                LOGGER.info("City entity is not found, creating a new one");

                CityEntity newCity = new CityEntity();
                newCity.setName(airportPromptModel.getCityName());
                newCity.setIataCode(airportPromptModel.getCityIata());

                //  Ensure TimezoneEntity is attached before setting it
                TimezoneEntity timezoneEntity = timezoneRepository.findByTimezone(airportPromptModel.getTimezone())
                        .orElseGet(() -> {
                            TimezoneEntity newTimezone = new TimezoneEntity();
                            newTimezone.setTimezone(airportPromptModel.getTimezone());
                            return timezoneRepository.saveAndFlush(newTimezone); // Save it first
                        });

                timezoneEntity = timezoneRepository.findById(timezoneEntity.getId()).orElseThrow(
                        () -> new RuntimeException("Failed to persist TimezoneEntity")
                ); // Ensure it's managed

                newCity.setTimezone(timezoneEntity); //  Now it's managed
                newCity = cityRepository.saveAndFlush(newCity); // Persist CityEntity

                return newCity;
            });

            LOGGER.info("City entity is " + cityEntity);
            airportEntity.setCity(cityEntity);
        }
    }
}
