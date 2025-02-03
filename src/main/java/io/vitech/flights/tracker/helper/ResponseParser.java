package io.vitech.flights.tracker.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vitech.flights.tracker.openai.model.AirportGptModel;
import org.springframework.stereotype.Service;

@Service
public class ResponseParser {

    private final ObjectMapper objectMapper;

    public ResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AirportGptModel parseResponse(String jsonResponse) {
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode choicesNode = rootNode.path("choices");
        if (choicesNode.isArray() && choicesNode.size() > 0) {
            JsonNode contentNode = choicesNode.get(0).path("message").path("content");
            if (!contentNode.isMissingNode()) {
                String content = contentNode.asText();
                JsonNode contentJsonNode = null;
                try {
                    contentJsonNode = objectMapper.readTree(content);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                boolean status = contentJsonNode.path("status").asBoolean();
                if (status) {
                    JsonNode dataNode = contentJsonNode.path("data");
                    try {
                        return objectMapper.treeToValue(dataNode, AirportGptModel.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return null;
    }
}