package io.vitech.flights.tracker.processor.impl;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.helper.AirportPair;
import io.vitech.flights.tracker.helper.ChunkProcessor;
import io.vitech.flights.tracker.helper.ResponseParser;
import io.vitech.flights.tracker.mapper.AirportMapper;
import io.vitech.flights.tracker.openai.OpenAIService;
import io.vitech.flights.tracker.openai.model.AirlinePromptModel;
import io.vitech.flights.tracker.openai.model.AirportPromptModel;
import io.vitech.flights.tracker.processor.BaseProcessor;
import io.vitech.flights.tracker.repository.AirlineReposity;
import io.vitech.flights.tracker.repository.AirportRepository;
import io.vitech.flights.tracker.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "vitech.tracker.processor.flight.enabled", havingValue = "true")
public class FlightProcessor extends BaseProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(FlightProcessor.class);

    @Value("${vitech.tracker.processor.flight.chunk-size}")
    private int chunkSize;

    private final ResponseParser responseParser;
    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;

    public FlightProcessor(final OpenAIService openAIService, final ResponseParser parser, final  FlightRepository flightRepository,
                           final AirportRepository airportRepository, final AirportMapper airportMapper) {
        super(openAIService);
        this.responseParser = parser;
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.airportMapper = airportMapper;
    }


    @Override
    public void process() {
        LOGGER.debug(PROCESSING_START_LOG_MSG_TEMPLATE, this.getClass().getSimpleName());

        final Set<AirportPromptModel> airportPromptModelSet = new HashSet<>();

        //get airline without fleet size
        airportPromptModelSet.addAll(flightRepository.findDistinctDepartureIataWhereAirportIsNull().stream()
                .map(a -> AirportPromptModel.builder().iata(a).build())
                .collect(Collectors.toSet()));
        airportPromptModelSet.addAll(flightRepository.findDistinctArrivalIataWhereAirportIsNull().stream()
                .map(a -> AirportPromptModel.builder().iata(a).build())
                .collect(Collectors.toSet()));

        if(airportPromptModelSet.isEmpty()) {
            LOGGER.debug("No airports to process.");
            return;
        }

        List<Set<AirportPromptModel>> chunks = ChunkProcessor.chunkify(airportPromptModelSet, chunkSize);

        try {
            for (Set<AirportPromptModel> chunk : chunks) {
                LOGGER.debug("Processing chunk: {}", chunk);
                // Process each chunk
                String structuredResponse = openAIService.getStructuredResponse(chunk);
                LOGGER.debug("StructuredResponse = {}", structuredResponse);

                List<AirportPromptModel> airports = responseParser.parseResponse(structuredResponse, AirportPromptModel.class);

                LOGGER.debug("Airports to save in DB = {}", airports);
                airports.forEach(airportPromptModel -> airportRepository.saveAndFlush(airportMapper.toEntity(airportPromptModel)));

                flightRepository.updateArrivalAirportIds();
                flightRepository.updateDepartureAirportIds();
            }
        } catch (Exception e) {
            LOGGER.error("Error processing flight airports", e);
            throw new RuntimeException(e);
        }
        LOGGER.debug(PROCESSING_END_MSG_TEMPLATE, this.getClass().getSimpleName());
    }
}
