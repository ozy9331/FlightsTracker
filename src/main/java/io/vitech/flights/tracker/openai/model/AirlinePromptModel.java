package io.vitech.flights.tracker.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AirlinePromptModel implements PromptModel {

    @JsonProperty
    private long id;
    @JsonProperty
    private String airlineName;
    @JsonProperty
    private String iata;
    @JsonProperty
    private String icao;
    @JsonProperty
    private Integer fleetSize;
    @JsonProperty
    private String foundedYear;

}
