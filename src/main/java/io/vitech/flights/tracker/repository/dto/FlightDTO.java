package io.vitech.flights.tracker.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class FlightDTO {

    private ZonedDateTime flightDate;
    private String departureAirport;
    private String departureCity;
    private String arrivalAirport;
    private String arrivalCity;
    private Double range;
    private String airline;
    private String aircraftType;

}
