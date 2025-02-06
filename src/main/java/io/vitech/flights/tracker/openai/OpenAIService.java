package io.vitech.flights.tracker.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vitech.flights.tracker.helper.JsonService;
import io.vitech.flights.tracker.helper.ResponseParser;
import io.vitech.flights.tracker.openai.model.AirportGptModel;
import io.vitech.flights.tracker.openai.template.ContentTemplate;
import io.vitech.flights.tracker.openai.template.GptRequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class OpenAIService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAIService.class);

    @Value("${vitech.tracker.openai.api.key}")
    private String apiKey;

    @Value("${vitech.tracker.openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final JsonService jsonService;
    private final ResponseParser responseParser;

    public OpenAIService(RestTemplate restTemplate, JsonService jsonService, ResponseParser responseParser, ResponseParser responseParser1) {
        this.restTemplate = restTemplate;
        this.jsonService = jsonService;
        this.responseParser = responseParser1;
    }

    public String getStructuredResponse(Set<AirportGptModel> requestObjects) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);

        String requestBody = String.format(ContentTemplate.REQUEST_CONTENT_TEMPLATE, jsonService.convertToJson(requestObjects));

        GptRequestTemplate contentTemplate = new GptRequestTemplate(requestBody);

        LOGGER.info("requestObject = " + contentTemplate.toJson());

        HttpEntity<String> entity = new HttpEntity<>(contentTemplate.toJson(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        return responseParser.parseJsonResponse(response.getBody());
    }
}
