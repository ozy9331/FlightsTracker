package io.vitech.flights.tracker.openai.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vitech.flights.tracker.openai.constant.ModelType;
import io.vitech.flights.tracker.openai.constant.RoleType;

import java.util.Collections;
import java.util.List;

public class GptRequestTemplate {

    @JsonProperty
    private String model;

    @JsonProperty
    private boolean store;

    @JsonProperty
    private List<Message> messages;


    public GptRequestTemplate(ModelType model, boolean store, RoleType role, String content) {
        this.model = model.toString();
        this.store = store;
        this.messages = Collections.singletonList(new Message(role, content));
    }

    public GptRequestTemplate(String content) {
        this(ModelType.GPT_4O_MINI, false, RoleType.USER, content);
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static class Message {
        @JsonProperty
        private String role;

        @JsonProperty
        private String content;

        public Message(RoleType role, String content) {
            this.role = role.toString();
            this.content = content;
        }
    }
}