package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.AirportEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {
    public List<AirportEntity> getAllAirports() {
        return List.of();
    }

    public AirportEntity getAirportById(int id) {
        return new AirportEntity();
    }
}
