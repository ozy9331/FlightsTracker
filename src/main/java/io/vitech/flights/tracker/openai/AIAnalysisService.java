package io.vitech.flights.tracker.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vitech.flights.tracker.exception.OpenAILimitExceedException;
import io.vitech.flights.tracker.helper.ChunkProcessor;
import io.vitech.flights.tracker.helper.JsonService;
import io.vitech.flights.tracker.helper.ResponseParser;
import io.vitech.flights.tracker.openai.template.ContentTemplate;
import io.vitech.flights.tracker.openai.template.GptRequestTemplate;
import io.vitech.flights.tracker.repository.FlightRepository;
import io.vitech.flights.tracker.repository.dto.FlightDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
public class AIAnalysisService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AIAnalysisService.class);
    @Value("${vitech.tracker.openai.api.key-statistic}")
    private String apiKey;

    @Value("${vitech.tracker.openai.api.url}")
    private String apiUrl;

    private FlightRepository flightRepository;
    private ResponseParser responseParser;
    private final JsonService jsonService;
    private final RestTemplate restTemplate;

    public AIAnalysisService(FlightRepository flightRepository, ResponseParser responseParser, JsonService jsonService, RestTemplate restTemplate) {
        this.flightRepository = flightRepository;
        this.responseParser = responseParser;
        this.jsonService = jsonService;
        this.restTemplate = restTemplate;
    }




    public String sendDataToAi(final Set<FlightDTO> flights) throws JsonProcessingException {


        List<FlightDTO> requestObjects = flightRepository.findAllFlightsInRangeDTO(null, null, null, null);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);


        String requestBody = String.format(ContentTemplate.REQUEST_AI_STATISTIC_CONTENT_TEMPLATE, jsonService.convertToJson(requestObjects));
        GptRequestTemplate contentTemplate = new GptRequestTemplate(requestBody);
        //TODO change to debug
        LOGGER.info("RequestObject = {}", contentTemplate.toJson());

        HttpEntity<String> entity = new HttpEntity<>(contentTemplate.toJson(), headers);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

        } catch (Exception e) {
            LOGGER.error("Error occurred while calling OpenAI API: {}", e.getMessage(), e);
            throw new OpenAILimitExceedException("Error occurred while calling OpenAI API. Please try again later.");
        }

        if(response.getStatusCode() == HttpStatusCode.valueOf(429)) {
            LOGGER.error("Too many requests to OpenAI API. Please try again later.");
            throw new OpenAILimitExceedException("Too many requests to OpenAI API. Please try again later.");
        }

        //TODO change to debug
        LOGGER.info("Response = {}", response);

        return responseParser.parseJsonResponse(response.getBody());
    }

    public String getTopStatistics(Double rangeStart, Double rangeEnd, LocalDate startDate, LocalDate endDate) throws JsonProcessingException {
        ZonedDateTime startDateTime = startDate != null ? startDate.atStartOfDay(ZoneId.systemDefault()) : null;
        ZonedDateTime endDateTime = endDate != null ? endDate.atStartOfDay(ZoneId.systemDefault()) : null;

        List<FlightDTO> requestObjects = flightRepository.findAllFlightsInRangeDTO(rangeStart, rangeEnd, startDateTime, endDateTime);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);


        String requestBody = String.format(ContentTemplate.REQUEST_AI_STATISTIC_CONTENT_TEMPLATE, jsonService.convertToJson(requestObjects));
        GptRequestTemplate contentTemplate = new GptRequestTemplate(requestBody);
        //TODO change to debug
        LOGGER.info("RequestObject = {}", contentTemplate.toJson());

        HttpEntity<String> entity = new HttpEntity<>(contentTemplate.toJson(), headers);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

        } catch (Exception e) {
            LOGGER.error("Error occurred while calling OpenAI API: {}", e.getMessage(), e);
            throw new OpenAILimitExceedException("Error occurred while calling OpenAI API. Please try again later.");
        }

        if(response.getStatusCode() == HttpStatusCode.valueOf(429)) {
            LOGGER.error("Too many requests to OpenAI API. Please try again later.");
            throw new OpenAILimitExceedException("Too many requests to OpenAI API. Please try again later.");
        }

        //TODO change to debug
        LOGGER.info("Response = {}", response);

        return responseParser.parseJsonResponse(response.getBody());
    }

}
