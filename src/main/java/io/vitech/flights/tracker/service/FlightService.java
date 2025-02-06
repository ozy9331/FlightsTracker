package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.FlightEntity;
import io.vitech.flights.tracker.repository.FlightRepository;
import io.vitech.flights.tracker.repository.impl.FlightRepositoryCustomImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepositoryCustomImpl flightRepositoryCustom;
    private final FlightRepository flightRepository;

    public FlightService(FlightRepositoryCustomImpl flightRepositoryCustom, FlightRepository flightRepository) {
        this.flightRepositoryCustom = flightRepositoryCustom;
        this.flightRepository = flightRepository;
    }

    public Page<FlightEntity> getAllFlights(Pageable pageable) {
        return flightRepository.findAll(pageable);
    }

    public FlightEntity getFlightById(int id) {
        return flightRepository.findById((long) id).orElseThrow();
    }

    public List<AirportEntity> getTopArrivalAirports(int limit) {
        return flightRepository.findTopArrivalAirports(PageRequest.of(0,limit));
    }

    public List<AirportEntity> getTopDepartureAirports(int limit) {
        return flightRepository.findTopDepartureAirports(PageRequest.of(0, limit));
    }

    public List<AirportEntity> getTopArrivalAirports(int limit, LocalDate startDate, LocalDate endDate, Integer rangeStart, Integer rangeEnd) {
        return flightRepositoryCustom.findTopArrivalAirports(PageRequest.of(0,limit), startDate,endDate,rangeStart,rangeEnd);
    }

    public List<AirportEntity> getTopDepartureAirports(int limit, LocalDate startDate, LocalDate endDate, Integer rangeStart, Integer rangeEnd) {
        return flightRepository.findTopDepartureAirports(PageRequest.of(0, limit));
    }

}
