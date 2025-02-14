package io.vitech.flights.tracker.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AirportPair {
    private Long departureAirportId;
    private Long arrivalAirportId;
    private Double departureLatitude;
    private Double departureLongitude;
    private Double arrivalLatitude;
    private Double arrivalLongitude;

    public AirportPair(Long depId, Long arrId, Double depLat, Double depLng, Double arrLat, Double arrLng) {
        // Always store the smaller ID first to avoid duplicates
        if (depId < arrId) {
            this.departureAirportId = depId;
            this.arrivalAirportId = arrId;
            this.departureLatitude = depLat;
            this.departureLongitude = depLng;
            this.arrivalLatitude = arrLat;
            this.arrivalLongitude = arrLng;
        } else {
            this.departureAirportId = arrId;
            this.arrivalAirportId = depId;
            this.departureLatitude = arrLat;
            this.departureLongitude = arrLng;
            this.arrivalLatitude = depLat;
            this.arrivalLongitude = depLng;
        }
    }

}
