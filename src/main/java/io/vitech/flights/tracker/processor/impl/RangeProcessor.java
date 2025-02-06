package io.vitech.flights.tracker.processor.impl;

import io.vitech.flights.tracker.openai.OpenAIService;
import io.vitech.flights.tracker.processor.BaseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(name = "vitech.tracker.processor.range.enabled", havingValue = "true")
public class RangeProcessor extends BaseProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(RangeProcessor.class);
    public RangeProcessor(OpenAIService openAIService) {
        super(openAIService);
    }

    @Override
    public void process() {
        LOGGER.debug(PROCESSING_START_LOG_MSG_TEMPLATE, this.getClass().getSimpleName());
        LOGGER.debug("I am working,  hop hop hop...");
        LOGGER.debug(PROCESSING_END_MSG_TEMPLATE, this.getClass().getSimpleName());
    }
}
