package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.CityEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    public List<CityEntity> getAllCities() {
        return List.of();
    }

    public CityEntity getCityById(int id) {
        return new CityEntity();
    }
}
