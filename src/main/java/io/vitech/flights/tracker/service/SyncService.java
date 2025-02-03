package io.vitech.flights.tracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SyncService {
    private static final Logger LOGGER  = LoggerFactory.getLogger(SyncService.class);

    // Simulate an async sync job
    @Async
    public void syncData() {
        try {
            LOGGER.info("Sync job started...");
            // Simulate long-running task (e.g., calling external APIs, database updates)
            Thread.sleep(5000);  // Simulate sync operation delay
            LOGGER.info("Sync job completed.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Sync job interrupted: ", e);
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