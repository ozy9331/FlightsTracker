package io.vitech.flights.tracker.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirportGptModel {
    @JsonProperty
    private String name;
    @JsonProperty
    private String iata;
    @JsonProperty
    private String cityName;
    @JsonProperty
    private String timezone;
}
