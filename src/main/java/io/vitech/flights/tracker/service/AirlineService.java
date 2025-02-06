package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.AirlineEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirlineService {
    public List<AirlineEntity> getAllAirlines() {
        return List.of();
    }

    public AirlineEntity getAirlineById(int id) {
        return new AirlineEntity();
    }
}
