package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.conf.PaginationConfig;
import io.vitech.flights.tracker.entity.AircraftEntity;
import io.vitech.flights.tracker.repository.AircraftRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AircraftService {

    private final AircraftRepository aircraftRepository;

    public AircraftService(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }
    public Page<AircraftEntity> getAllAircrafts(PageRequest pageRequest) {
        return aircraftRepository.findAll(pageRequest);
    }

    public AircraftEntity getAircraftById(int id) {
        return aircraftRepository.findById((long) id).orElseThrow();
    }


}
