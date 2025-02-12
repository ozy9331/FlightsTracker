package io.vitech.flights.tracker.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vitech.flights.tracker.openai.model.PromptModel;
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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cities")
public class CityEntity implements PromptModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonProperty
    @Column(name="city_name")
    private String name;

    @JsonProperty
    @Column(name = "iata_code")
    private String iataCode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "timezone_id", referencedColumnName = "id")
    private TimezoneEntity timezone;
}
