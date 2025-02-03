package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.CityEntity;
import io.vitech.flights.tracker.openai.OpenAIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SyncService {
    private static final Logger LOGGER  = LoggerFactory.getLogger(SyncService.class);

    OpenAIService openAIService;

    public SyncService(final OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    // Simulate an async sync job
    @Async
    public void syncData() {
        try {
            LOGGER.info("Sync job started...");


            AirportEntity airportEntity =  AirportEntity.builder().name("Aishalton").build();
            CityEntity cityEntity = CityEntity.builder().name("Georgetown").build();



            LOGGER.info("Response from OpenAI API: " + openAIService.getStructuredResponse(airportEntity));
           // LOGGER.info("Response from OpenAI API: " + openAIService.getStructuredResponse(cityEntity));
            // Simulate long-running task (e.g., calling external APIs, database updates)
            LOGGER.info("Sync job completed.");


        } catch (Exception e) {
            LOGGER.error("Synch job failed.", e);
        }
    }

    // The @PostConstruct method runs after the bean has been initialized
    @PostConstruct
    public void init() {
        LOGGER.info("Initializing SyncService...");
        // Start syncData when the bean is initialized
        syncData();
    }
}