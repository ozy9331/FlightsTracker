package io.vitech.flights.tracker.processor.impl;

import io.vitech.flights.tracker.helper.ChunkProcessor;
import io.vitech.flights.tracker.helper.ResponseParser;
import io.vitech.flights.tracker.openai.OpenAIService;
import io.vitech.flights.tracker.openai.model.AirlinePromptModel;
import io.vitech.flights.tracker.processor.BaseProcessor;
import io.vitech.flights.tracker.repository.AirlineReposity;
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
@ConditionalOnProperty(name = "vitech.tracker.processor.airline.enabled", havingValue = "true")
public class AirlineProcessor extends BaseProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(AirlineProcessor.class);

    @Value("${vitech.tracker.processor.airline.chunk-size}")
    private int chunkSize;

    ResponseParser responseParser;
    AirlineReposity airlineReposity;

    public AirlineProcessor(final OpenAIService openAIService,final ResponseParser parser, final AirlineReposity airlineReposity) {
        super(openAIService);
        this.responseParser = parser;
        this.airlineReposity = airlineReposity;
    }

    @Override
    public void process() {
        LOGGER.debug(PROCESSING_START_LOG_MSG_TEMPLATE, this.getClass().getSimpleName());

        final Set<AirlinePromptModel> airlinePromptModels = new HashSet<>();

        //get airline without fleet size
        airlinePromptModels.addAll(airlineReposity.findAllWithNullName().stream()
                .map(a -> AirlinePromptModel.builder()
                                                        .id(a.getId())
                                                        .airlineName(a.getName())
                                                        .iata(a.getIataCode())
                                                        .icao(a.getIcaoCode())
                                                        .fleetSize(a.getFleetSize())
                                                        .foundedYear(a.getDateFounded())
                                                        .build())
                .collect(Collectors.toSet()));

        if(airlinePromptModels.isEmpty()) {
            LOGGER.debug("No airline to process.");
            return;
        }

        List<Set<AirlinePromptModel>> chunks = ChunkProcessor.chunkify(airlinePromptModels, chunkSize);

        try {
            for (Set<AirlinePromptModel> chunk : chunks) {
                LOGGER.debug("Processing chunk: {}", chunk);
                // Process each chunk
                String structuredResponse = openAIService.getStructuredResponse(chunk);
                LOGGER.debug("StructuredResponse = {}", structuredResponse);

                List<AirlinePromptModel> airline = responseParser.parseResponse(structuredResponse, AirlinePromptModel.class);
                LOGGER.debug("Airline = {}", airline);
                airline.forEach(airlinePromptModel -> {
                    airlineReposity.updateAirlineById(airlinePromptModel.getId(), airlinePromptModel.getAirlineName(), airlinePromptModel.getIata(), airlinePromptModel.getIcao(), airlinePromptModel.getFleetSize(), airlinePromptModel.getFoundedYear());
                });
            }
        } catch (Exception e) {
            LOGGER.error("Error processing airports", e);
            throw new RuntimeException(e);
        }
        LOGGER.debug(PROCESSING_END_MSG_TEMPLATE, this.getClass().getSimpleName());
    }
}
