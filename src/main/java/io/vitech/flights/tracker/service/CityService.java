package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.CityEntity;
import io.vitech.flights.tracker.repository.CityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
    public Page<CityEntity> getAllCities(PageRequest pageRequest) {
       return cityRepository.findAll(pageRequest);
    }

    public CityEntity getCityById(int id) {
        return cityRepository.findById((long) id).orElseThrow();
    }
}
