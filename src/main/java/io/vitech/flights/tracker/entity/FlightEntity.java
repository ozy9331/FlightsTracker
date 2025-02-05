package io.vitech.flights.tracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "flight")
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private ZonedDateTime flightDate;
    private String departureTime;
    private String arrivalTime;
    private Integer range;

    private String departureIataCode;
    private String arrivalIataCode;
    private String airlineIata;
    private String airlineIcao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "flight_status_id", referencedColumnName = "id")
    private FlightStatusEntity flightStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "arrival_airport_id", referencedColumnName = "id")
    private AirportEntity arrivalAirport;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "departure_airport_id", referencedColumnName = "id")
    private AirportEntity departureAirport;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aircraft_id", referencedColumnName = "id")
    private AircraftEntity aircraft;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "airline_id", referencedColumnName = "id")
    private AirlineEntity airline;
}
