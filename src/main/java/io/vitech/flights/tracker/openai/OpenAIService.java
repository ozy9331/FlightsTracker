package io.vitech.flights.tracker.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vitech.flights.tracker.helper.JsonService;
import io.vitech.flights.tracker.helper.ResponseParser;
import io.vitech.flights.tracker.openai.model.GptRequestModel;
import io.vitech.flights.tracker.openai.template.ContentTemplate;
import io.vitech.flights.tracker.openai.template.GptRequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final JsonService jsonService;
    private final ResponseParser responseParser;

    public OpenAIService(RestTemplate restTemplate, JsonService jsonService, ResponseParser responseParser, ResponseParser responseParser1) {
        this.restTemplate = restTemplate;
        this.jsonService = jsonService;
        this.responseParser = responseParser1;
    }

    public String getStructuredResponse(GptRequestModel requestObject) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);

        String requestBody = String.format(ContentTemplate.REQUEST_CONTENT_TEMPLATE, jsonService.convertToJson(requestObject));

        GptRequestTemplate contentTemplate = new GptRequestTemplate(requestBody);

        System.out.println("requestObject = " + contentTemplate.toJson());

        HttpEntity<String> entity = new HttpEntity<>(contentTemplate.toJson(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        System.out.println("response = " + response.getBody());
        System.out.println("Parsed response = " + responseParser.parseResponse(response.getBody()).toString());
        return response.getBody();
    }
}
