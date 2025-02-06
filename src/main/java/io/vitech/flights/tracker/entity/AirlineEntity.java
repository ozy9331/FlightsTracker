package io.vitech.flights.tracker.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "airline")
public class AirlineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "airline_name")
    private String name;

    // The icao_code is a unique four-letter alphanumeric code assigned by the International Civil Aviation Organization (ICAO) to each airport around the world.
    // It is used for air traffic control and airline operations such as flight planning.
    @Column(name = "icao_code")
    private String icaoCode;

    @Column(name = "iata_code")
    private String iataCode;
}
