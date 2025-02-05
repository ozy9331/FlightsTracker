package io.vitech.flights.tracker.processor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vitech.flights.tracker.helper.ChunkProcessor;
import io.vitech.flights.tracker.helper.ResponseParser;
import io.vitech.flights.tracker.mapper.AirportMapper;
import io.vitech.flights.tracker.openai.OpenAIService;
import io.vitech.flights.tracker.openai.model.AirportGptModel;
import io.vitech.flights.tracker.processor.BaseProcessor;
import io.vitech.flights.tracker.repository.AirportRepository;
import io.vitech.flights.tracker.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AirportProcessor extends BaseProcessor {
    private static Logger LOGGER = LoggerFactory.getLogger(AirportProcessor.class);

    @Value("${airport.chunk.size}")
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
        final Set<AirportGptModel> airportGptModels = new HashSet<>();

        airportGptModels.addAll(flightRepository.findAllByNullDepartureAirportIdAndStatus("LANDED").stream()
                .map(f -> AirportGptModel.builder().iata(f.getDepartureIataCode()).build())
                .collect(Collectors.toSet()));

        airportGptModels.addAll(flightRepository.findAllByNullArrivalAirportIdAndStatus("LANDED").stream()
                .map(f -> AirportGptModel.builder().iata(f.getArrivalIataCode()).build())
                .collect(Collectors.toSet()));

        if(airportGptModels.isEmpty()) {
            LOGGER.debug("No flights airports to process.");
            return;
        }

        List<Set<AirportGptModel>> chunks = ChunkProcessor.chunkify(airportGptModels, chunkSize);

        try {
            for (Set<AirportGptModel> chunk : chunks) {
                LOGGER.debug("Processing chunk: " + chunk);
                // Process each chunk
                String structuredResponse = openAIService.getStructuredResponse(chunk);
                LOGGER.info("structuredResponse = " + structuredResponse);

                List<AirportGptModel> airports = responseParser.parseResponse(structuredResponse, AirportGptModel.class);
                airports.forEach(airportGptModel -> {
                    if (airportRepository.findByIataCode(airportGptModel.getIata()).isEmpty()) {
                        LOGGER.info("Airport DOEST EXIST: " + airportGptModel);
                        airportRepository.save(airportMapper.toEntity(airportGptModel));
                    }else {
                        LOGGER.info("Airport EXISTS: " + airportGptModel);
                    }
                });
            }

        } catch (JsonProcessingException e) {
            LOGGER.error("Error processing airports", e);
            throw new RuntimeException(e);
        }

        LOGGER.debug("Processing airports end");
    }
}
