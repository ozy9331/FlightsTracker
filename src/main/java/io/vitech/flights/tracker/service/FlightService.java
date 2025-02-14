package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.FlightEntity;
import io.vitech.flights.tracker.repository.FlightRepository;
import io.vitech.flights.tracker.repository.dto.BusiestDayResponse;
import io.vitech.flights.tracker.repository.dto.TopDestinationDTO;
import io.vitech.flights.tracker.repository.impl.FlightRepositoryCustomImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepositoryCustomImpl flightRepositoryCustom;
    private final FlightRepository flightRepository;

    public FlightService(FlightRepositoryCustomImpl flightRepositoryCustom, FlightRepository flightRepository) {
        this.flightRepositoryCustom = flightRepositoryCustom;
        this.flightRepository = flightRepository;
    }

    public Page<FlightEntity> getAllFlights(Pageable pageable, Integer rangeStart, Integer rangeEnd) {
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

    public List<TopDestinationDTO> getTopDestinations(int limit, Double startRange, Double endRange) {
        return flightRepository.findTopDestinations(limit, startRange, endRange)
                .stream()
                .map(row -> new TopDestinationDTO(
                        ((Number) row[0]).longValue(),  // airportId
                        (String) row[1],               // airportName
                        ((Number) row[2]).longValue()  // totalFlights
                ))
                .toList();
    }

    public List<BusiestDayResponse> getBusiestDays(Double startRange, Double endRange) {
        List<Object[]> results = flightRepository.findBusiestDays(startRange, endRange);

        return results.stream()
                .map(obj -> new BusiestDayResponse((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }

}
