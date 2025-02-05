package io.vitech.flights.tracker.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseParser.class);

    private final ObjectMapper objectMapper;

    public ResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> List <T> parseResponse(final String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String parseJsonResponse(String jsonString) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);

            // Extract the 'content' field
            String content = rootNode.path("choices").get(0).path("message").path("content").asText();

            // Remove the ```json wrapper
            content = content.replace("```json", "").replace("```", "").trim();

            // Parse content as JSON
            JsonNode contentNode = objectMapper.readTree(content);
            // Check if status is true
            if (contentNode.path("status").asBoolean()) {
                JsonNode dataNode = contentNode.path("data");
                LOGGER.debug("Extracted Data: " + dataNode.toPrettyString());
                return dataNode.toPrettyString();
            } else {
                LOGGER.warn("Status is false. No data extracted." + jsonString);
                throw new IllegalArgumentException("No data extracted.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}