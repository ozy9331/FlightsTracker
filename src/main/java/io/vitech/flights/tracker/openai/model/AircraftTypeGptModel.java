package io.vitech.flights.tracker.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class AircraftTypeGptModel implements GptRequestModel{

    @JsonProperty
    private String IataShortCode;
    @JsonProperty
    private String aircraftType;
}
