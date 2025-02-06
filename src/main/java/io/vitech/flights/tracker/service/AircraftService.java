package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.AircraftEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AircraftService {
    public List<AircraftEntity> getAllAircrafts() {
        return List.of();
    }

    public AircraftEntity getAircraftById(int id) {

        return new AircraftEntity();
    }


}
