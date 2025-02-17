package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.FlightEntity;
import io.vitech.flights.tracker.repository.FlightRepository;
import io.vitech.flights.tracker.repository.dto.BusiestDayResponse;
import io.vitech.flights.tracker.repository.dto.TopAircraftResponse;
import io.vitech.flights.tracker.repository.dto.TopAirlineResponse;
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

    public List<TopDestinationDTO> getTopDestinations(int limit, Double startRange, Double endRange, LocalDate startDate, LocalDate endDate) {
        return flightRepository.findTopDestinations(limit, startRange, endRange, startDate, endDate)
                .stream()
                .map(row -> new TopDestinationDTO(
                        ((Number) row[0]).longValue(),  // airportId
                        (String) row[1],               // airportName
                        ((Number) row[2]).longValue()  // totalFlights
                ))
                .toList();
    }

    public List<BusiestDayResponse> getBusiestDays(Double startRange, Double endRang, LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = flightRepository.findBusiestDays(startRange, endRang, startDate, endDate);

        return results.stream()
                .map(obj -> new BusiestDayResponse((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }



    public List<TopAirlineResponse> findTopAirlines(Double startRange, Double endRange, Integer limit, LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = flightRepository.findTopAirlines(startRange, endRange, startDate, endDate);

        return results.stream()
                .map(row -> new TopAirlineResponse(
                        (String) row[0],   // airline_name
                        (String) row[1],   // iata_code
                        ((Number) row[2]).longValue() // totalFlights
                ))
                .limit(limit != null ? limit : Integer.MAX_VALUE) // Apply limit dynamically
                .toList();
    }

    public List<TopAircraftResponse> findTopAircrafts(Double startRange, Double endRange, Integer limit, LocalDate startDate, LocalDate endDate) {

        if (limit == null) limit = 10; // Default to top 10 if no limit is given

        List<Object[]> results = flightRepository.findTopAircrafts(startRange, endRange, startDate, endDate);

        return results.stream()
                .map(row -> new TopAircraftResponse((String) row[0], ((Number) row[1]).longValue()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
