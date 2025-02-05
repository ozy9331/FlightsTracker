package io.vitech.flights.tracker.processor;

import io.vitech.flights.tracker.openai.OpenAIService;

public abstract class BaseProcessor {
    protected OpenAIService openAIService;

    public BaseProcessor(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    public abstract void process();
}
