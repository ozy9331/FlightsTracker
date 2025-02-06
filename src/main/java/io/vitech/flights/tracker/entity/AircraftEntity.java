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
@Table(name = "aircraft")
public class AircraftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "aircraft_reg", nullable = false, unique = true)
    private String registration;

    @Column(name = "aircraft_model")
    private String model;

    @Column(name = "aita_assignment")
    private String aitaAssignment;

    @Column(name = "aircraft_age")
    private Float age;
}
