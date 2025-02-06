package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.FlightEntity;
import io.vitech.flights.tracker.repository.FlightRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Page<FlightEntity> getAllFlights(Pageable pageable) {
        return flightRepository.findAll(pageable);
    }

    public FlightEntity getFlightById(int id) {
        return flightRepository.findById((long) id).orElseThrow();
    }
}
