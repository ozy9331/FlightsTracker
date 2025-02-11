package io.vitech.flights.tracker.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "aircraft")
public class AircraftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "aircraft_reg", nullable = false)
    private String registration;

    @Column(name = "iata_code", nullable = false)
    private String iataCode;

    @Column(name = "aircraft_age")
    private Float age;

    @Column(name = "first_flight_date", nullable = true)
    private String firstFlightDate;

    @Column(name = "delivery_date", nullable = true)
    private String deliveryDate;

    @Column(name = "registration_date", nullable = true)
    private String registrationDate;

    @Column(name = "construction_number ")
    private String constructionNumber ;

    @Column(name = "plane_owner", nullable = false)
    private String planeOwner;

    @Column(name = "airline_iata_code")
    private String airlineIataCode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aircraft_type_id", referencedColumnName = "id")
    private AircraftType aircraftType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "airline_id", referencedColumnName = "id")
    private AirlineEntity airlineEntity;


}
