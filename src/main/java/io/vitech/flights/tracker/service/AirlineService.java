package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.AirlineEntity;
import io.vitech.flights.tracker.helper.Validator;
import io.vitech.flights.tracker.repository.AirlineReposity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AirlineService {

    private final AirlineReposity airlineReposity;

    public AirlineService(AirlineReposity airlineReposity) {
        this.airlineReposity = airlineReposity;
    }

    public Page<AirlineEntity> getAllAirlines(PageRequest pageRequest, String name) {
        if(Validator.isValidAutocomplete(name)) {
            return airlineReposity.findByName(name, pageRequest);
        }
        return airlineReposity.findAll(pageRequest);
    }

    public AirlineEntity getAirlineById(int id) {
        return airlineReposity.findById((long) id).orElseThrow();
    }
}
