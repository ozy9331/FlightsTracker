package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.processor.BaseProcessor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyncService {
    private static final Logger LOGGER  = LoggerFactory.getLogger(SyncService.class);

    private final List<BaseProcessor> recordProcessors;

    public SyncService(List<BaseProcessor> recordProcessors) {
        this.recordProcessors = recordProcessors;
    }

    @Async
    public void syncData() {
        try {
            LOGGER.info("Sync job started.");
            for (BaseProcessor processor : recordProcessors) {
                processor.process();
            }
            LOGGER.info("Sync job completed.");

        } catch (Exception e) {
            LOGGER.error("Sync job failed.", e);
        }
    }

    // The @PostConstruct method runs after the bean has been initialized
    @PostConstruct
    public void init() {

        Thread daemonThread = new Thread(this::syncData);
        daemonThread.setDaemon(true);
        daemonThread.start();
        LOGGER.info("Sync job triggered as daemon thread [{}].", daemonThread.getName());
    }
}