package io.vitech.flights.tracker.entity;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "flight")
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int arrivalAirportId;
    private int departureAirportId;
    private ZonedDateTime flightDate;
    private int flightStatusId;
    private int aircraftId;
}
