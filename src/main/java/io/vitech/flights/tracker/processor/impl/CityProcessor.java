package io.vitech.flights.tracker.processor.impl;

import io.vitech.flights.tracker.openai.OpenAIService;
import io.vitech.flights.tracker.processor.BaseProcessor;
import org.springframework.stereotype.Component;

@Component
public class CityProcessor extends BaseProcessor {



    public CityProcessor(OpenAIService openAIService) {
        super(openAIService);
    }

    @Override
    public void process() {
        System.out.println("Processing cities");
    }
}
