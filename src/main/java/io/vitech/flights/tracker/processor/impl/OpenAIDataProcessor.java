package io.vitech.flights.tracker.processor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vitech.flights.tracker.helper.ChunkProcessor;
import io.vitech.flights.tracker.openai.AIAnalysisService;
import io.vitech.flights.tracker.repository.FlightRepository;
import io.vitech.flights.tracker.repository.dto.FlightDTO;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@ConditionalOnProperty(name = "vitech.tracker.processor.openai.enabled", havingValue = "true")
public class OpenAIDataProcessor  {

    private static Logger LOGGER = LoggerFactory.getLogger(OpenAIDataProcessor.class);

    @Value("${vitech.tracker.processor.openai.chunk-size}")
    private int chunkSize;

    FlightRepository flightRepository;
    AIAnalysisService aiAnalysisService;

    public OpenAIDataProcessor(final FlightRepository flightRepository, final AIAnalysisService aiAnalysisService) {
        this.flightRepository = flightRepository;
    }

    @PostConstruct
    public void process() {
        LOGGER.debug("Open AI upload data started.", this.getClass().getSimpleName());
        List<FlightDTO> requestObjects = flightRepository.findAllFlightsInRangeDTO(null, null, null, null);

        List<Set<FlightDTO>> chunks = ChunkProcessor.chunkify(new HashSet<>(requestObjects), chunkSize);

        try {

            for (Set<FlightDTO> chunk : chunks) {
                LOGGER.debug("Processing chunk: {}", chunk);
                // Process each chunk
                String structuredResponse = aiAnalysisService.sendDataToAi(chunk);
            }

        } catch (JsonProcessingException e) {
            LOGGER.error("Error processing airports", e);
            throw new RuntimeException(e);
        }

        LOGGER.debug("Open AI upload data ended.", this.getClass().getSimpleName());
    }
}
