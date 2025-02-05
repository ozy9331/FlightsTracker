package io.vitech.flights.tracker.entity;

import io.vitech.flights.tracker.openai.model.GptRequestModel;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "airport")
public class AirportEntity implements GptRequestModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "airport_name")
    private String name;

    //The iata_code of an airport is a unique three-letter code assigned by the International Air Transport Association (IATA) to identify airports around the world.
    // This code is commonly used in airline timetables, tickets, and baggage tags.

    @Column(name = "iata_code")
    private String iataCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private CityEntity city;
}
