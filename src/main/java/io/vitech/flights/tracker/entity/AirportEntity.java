package io.vitech.flights.tracker.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airport")
public class AirportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "airport_name", nullable = false)
    private String name;

    //The iata_code of an airport is a unique three-letter code assigned by the International Air Transport Association (IATA) to identify airports around the world.
    // This code is commonly used in airline timetables, tickets, and baggage tags.
    @Column(name = "iata_code", nullable = false)
    private String iata_code;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private CityEntity city;
}
