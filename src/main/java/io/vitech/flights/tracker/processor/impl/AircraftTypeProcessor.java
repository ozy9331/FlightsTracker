package io.vitech.flights.tracker.processor.impl;

import io.vitech.flights.tracker.entity.AircraftType;
import io.vitech.flights.tracker.helper.ChunkProcessor;
import io.vitech.flights.tracker.helper.ResponseParser;
import io.vitech.flights.tracker.openai.OpenAIService;
import io.vitech.flights.tracker.openai.model.AircraftTypeGptModel;
import io.vitech.flights.tracker.processor.BaseProcessor;
import io.vitech.flights.tracker.repository.AircraftRepository;
import io.vitech.flights.tracker.repository.AircraftTypeRepository;
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
@ConditionalOnProperty(name = "vitech.tracker.processor.aircraft-type.enabled", havingValue = "true")
public class AircraftTypeProcessor extends BaseProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(AircraftTypeProcessor.class);

    @Value("${vitech.tracker.processor.aircraft-type.chunk-size}")
    private int chunkSize;

    ResponseParser responseParser;
    AircraftRepository aircraftRepository;
    AircraftTypeRepository aircraftTypeRepository;

    public AircraftTypeProcessor(final OpenAIService openAIService, final ResponseParser responseParser, final AircraftRepository aircraftRepository, final AircraftTypeRepository aircraftTypeRepository) {
        super(openAIService);
        this.responseParser = responseParser;
        this.aircraftRepository = aircraftRepository;
        this.aircraftTypeRepository = aircraftTypeRepository;
    }
    @Override
    public void process() {
        LOGGER.debug(PROCESSING_START_LOG_MSG_TEMPLATE, this.getClass().getSimpleName());

        final Set<AircraftTypeGptModel> aircraftTypeGptModels = new HashSet<>();

        aircraftTypeGptModels.addAll(aircraftRepository.findAllWithNullAircraftType().stream()
                .map(a -> AircraftTypeGptModel.builder().IataShortCode(a.getIataCode()).build())
                .collect(Collectors.toSet()));

        if(aircraftTypeGptModels.isEmpty()) {
            LOGGER.debug("No aircraft type to process.");
            return;
        }

        List<Set<AircraftTypeGptModel>> chunks = ChunkProcessor.chunkify(aircraftTypeGptModels, chunkSize);

        try {
            for (Set<AircraftTypeGptModel> chunk : chunks) {
                LOGGER.debug("Processing chunk: {}", chunk);
                // Process each chunk
                String structuredResponse = openAIService.getStructuredResponse(chunk);
                LOGGER.debug("StructuredResponse = {}", structuredResponse);

                List<AircraftTypeGptModel> aircraftModels = responseParser.parseResponse(structuredResponse, AircraftTypeGptModel.class);
                aircraftModels.forEach(aircraftTypeGptModel -> {
                    if (StringUtils.isNotBlank(aircraftTypeGptModel.getAircraftType()) && aircraftTypeRepository.findByIataCodeAndType(aircraftTypeGptModel.getIataShortCode(), aircraftTypeGptModel.getAircraftType()).isEmpty()) {

                        LOGGER.debug("Aircraft type [{}] DOES NOT EXIST in DB. " , aircraftTypeGptModel);
                        aircraftTypeRepository.save(AircraftType.builder().type(aircraftTypeGptModel.getAircraftType()).iataCode(aircraftTypeGptModel.getIataShortCode()).build());
                    }else {
                        LOGGER.debug("Airport [{}] skipped and will not be saved to DB.", aircraftTypeGptModel);
                    }
                });

            }

            aircraftRepository.updateAircraftTypeIdWhereNull();
        } catch (Exception e) {
            LOGGER.error("Error processing airports", e);
            throw new RuntimeException(e);
        }
        LOGGER.debug(PROCESSING_END_MSG_TEMPLATE, this.getClass().getSimpleName());
    }
}
