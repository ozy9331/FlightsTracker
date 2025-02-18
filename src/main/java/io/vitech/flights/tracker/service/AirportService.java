package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.helper.Validator;
import io.vitech.flights.tracker.repository.AirportRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public Page<AirportEntity> getAllAirports(PageRequest pageRequest,  String name, String city) {
        if (Validator.isValidAutocomplete(name)){
            if(Validator.isValidAutocomplete(city)) {
                System.out.println("City is valid and name is valid");
                return airportRepository.findByNameAndCityName(name, city, pageRequest);
            }

            return airportRepository.findByName(name, pageRequest);
        }

        if (Validator.isValidAutocomplete(city)) {
            System.out.println("City is valid");
            return airportRepository.findByCityName(city, pageRequest);
        }

        return airportRepository.findAll(pageRequest);
    }

    public AirportEntity getAirportById(int id) {
        return airportRepository.findById((long) id).orElseThrow();
    }
}
