package io.vitech.flights.tracker.processor.impl;

import io.vitech.flights.tracker.openai.OpenAIService;
import io.vitech.flights.tracker.processor.BaseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CityProcessor extends BaseProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CityProcessor.class);


    public CityProcessor(OpenAIService openAIService) {
        super(openAIService);
    }

    @Override
    public void process() {
        LOGGER.debug(PROCESSING_START_LOG_MSG_TEMPLATE, this.getClass().getSimpleName());
        LOGGER.debug(PROCESSING_END_MSG_TEMPLATE, this.getClass().getSimpleName());
    }
}
