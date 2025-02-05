package io.vitech.flights.tracker.processor;

import io.vitech.flights.tracker.openai.OpenAIService;

public abstract class BaseProcessor {
    protected static final String PROCESSING_START_LOG_MSG_TEMPLATE = "[{}] -- Processing start.";
    public static final String PROCESSING_END_MSG_TEMPLATE = "[{}] -- Processing end.";

    protected OpenAIService openAIService;

    public BaseProcessor(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    public abstract void process();
}
