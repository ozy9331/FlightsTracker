package io.vitech.flights.tracker.processor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vitech.flights.tracker.helper.ChunkProcessor;
import io.vitech.flights.tracker.helper.ResponseParser;
import io.vitech.flights.tracker.mapper.AirportMapper;
import io.vitech.flights.tracker.openai.OpenAIService;
import io.vitech.flights.tracker.openai.model.AirportPromptModel;
import io.vitech.flights.tracker.processor.BaseProcessor;
import io.vitech.flights.tracker.repository.AirportRepository;
import io.vitech.flights.tracker.repository.FlightRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "openai.processor.airport.enabled", havingValue = "true")
public class AirportProcessor extends BaseProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(AirportProcessor.class);

    @Value("${vitech.tracker.processor.airport.chunk-size}")
    private int chunkSize;

    FlightRepository flightRepository;
    AirportRepository airportRepository;
    ResponseParser responseParser;
    AirportMapper airportMapper;

    public AirportProcessor(final OpenAIService openAIService, final FlightRepository flightRepository, final AirportRepository airportRepository, final ResponseParser responseParser, final AirportMapper airportMapper) {
        super(openAIService);
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.responseParser = responseParser;
        this.airportMapper = airportMapper;
    }

    @Override
    public void process() {
        LOGGER.debug(PROCESSING_START_LOG_MSG_TEMPLATE, this.getClass().getSimpleName());

        final Set<AirportPromptModel> airportPromptModels = new HashSet<>();

        airportPromptModels.addAll(flightRepository.findAllByNullDepartureAirportIdAndStatus("LANDED").stream()
                .map(f -> AirportPromptModel.builder().iata(f.getDepartureIataCode()).build())
                .collect(Collectors.toSet()));

        airportPromptModels.addAll(flightRepository.findAllByNullArrivalAirportIdAndStatus("LANDED").stream()
                .map(f -> AirportPromptModel.builder().iata(f.getArrivalIataCode()).build())
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
                String structuredResponse = ""; openAIService.getStructuredResponse(chunk);
                LOGGER.debug("StructuredResponse = {}", structuredResponse);

                List<AirportPromptModel> airports = responseParser.parseResponse(structuredResponse, AirportPromptModel.class);
                airports.forEach(airportPromptModel -> {
                    if (StringUtils.isNotBlank(airportPromptModel.getName()) && airportRepository.findByIataCodeAndName(airportPromptModel.getIata(), airportPromptModel.getName()).isEmpty()) {

                        LOGGER.debug("Airport [{}] DOES NOT EXIST in DB. " , airportPromptModel);
                        airportRepository.save(airportMapper.toEntity(airportPromptModel));
                    }else {
                        LOGGER.debug("Airport [{}] skipped and will not be saved to DB.", airportPromptModel);
                    }
                });
            }

        } catch (JsonProcessingException e) {
            LOGGER.error("Error processing airports", e);
            throw new RuntimeException(e);
        }
        LOGGER.debug(PROCESSING_END_MSG_TEMPLATE, this.getClass().getSimpleName());
    }
}
