package io.vitech.flights.tracker.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AirportPromptModel implements PromptModel {
    @JsonProperty
    private String name;
    @JsonProperty
    private String iata;
    @JsonProperty
    private String cityName;
    @JsonProperty
    private String timezone;
}
