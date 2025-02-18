package io.vitech.flights.tracker.service;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.FlightEntity;
import io.vitech.flights.tracker.repository.FlightRepository;
import io.vitech.flights.tracker.repository.dto.BusiestDay;
import io.vitech.flights.tracker.repository.dto.TopAircraft;
import io.vitech.flights.tracker.repository.dto.TopAirline;
import io.vitech.flights.tracker.repository.dto.TopCity;
import io.vitech.flights.tracker.repository.dto.TopDestination;
import io.vitech.flights.tracker.repository.impl.FlightRepositoryCustomImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public List<AirportEntity> getTopArrivalAirports(final Integer limit) {
        return flightRepository.findTopArrivalAirports(PageRequest.of(0,limit));
    }

    public List<AirportEntity> getTopDepartureAirports(final Integer limit) {
        return flightRepository.findTopDepartureAirports(PageRequest.of(0, limit));
    }

    public List<AirportEntity> getTopArrivalAirports(final Integer limit, final LocalDate startDate, final LocalDate endDate, final Integer rangeStart, final Integer rangeEnd) {
        return flightRepositoryCustom.findTopArrivalAirports(PageRequest.of(0,limit), startDate,endDate,rangeStart,rangeEnd);
    }

    public List<AirportEntity> getTopDepartureAirports(final Integer limit, final LocalDate startDate, final LocalDate endDate, final Integer rangeStart, final Integer rangeEnd) {
        return flightRepository.findTopDepartureAirports(PageRequest.of(0, limit));
    }

    public List<TopDestination> getTopDestinations( final Integer limit, final Double startRange, final Double endRange, final LocalDate startDate, final LocalDate endDate, final String cityName, String airport) {
        return flightRepository.findTopDestinations(limit, startRange, endRange, startDate, endDate, cityName, airport)
                .stream()
                .map(row -> new TopDestination(
                        ((Number) row[0]).longValue(),  // airportId
                        (String) row[1],               // airportName
                        ((Number) row[2]).longValue(),  // totalFlights
                        (String) row[3]               // airportName
                ))
                .toList();
    }

    public List<BusiestDay> getBusiestDays(final Double startRange,final Double endRang, final LocalDate startDate, final LocalDate endDate, final String city, final String airport, Integer limit) {
        List<Object[]> results = flightRepository.findBusiestDays(startRange, endRang, startDate, endDate, city, airport);

        return results.stream()
                .map(obj -> new BusiestDay((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<TopAirline> findTopAirlines(final Double startRange, final Double endRange, final Integer limit, final LocalDate startDate, final LocalDate endDate, final String city, final String airport) {
        List<Object[]> results = flightRepository.findTopAirlines(startRange, endRange, startDate, endDate, limit);

        return results.stream()
                .map(row -> new TopAirline(
                        (String) row[0],   // airline_name
                        (String) row[1],   // iata_code
                        ((Number) row[2]).longValue() // totalFlights
                )).toList();
    }

    public List<TopAircraft> findTopAircrafts(final Double startRange, final Double endRange, final Integer limit, final LocalDate startDate, final LocalDate endDate, final String city, final String airport) {
        List<Object[]> results = flightRepository.findTopAircrafts(startRange, endRange, startDate, endDate, limit, city, airport);

        return results.stream()
                .map(row -> new TopAircraft((String) row[0], ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }

    public List<TopCity> findTopDestinationCities(final Double startRange, final Double endRange, final Integer limit, final String  aircraftType, final LocalDate startDate, final LocalDate endDate) {
            List<Object[]> results = flightRepository.findTopDestinationCities(aircraftType, startDate, endDate, limit, startRange, endRange);

            List<TopCity> destinationList = new ArrayList<>();
            for (Object[] row : results) {
                destinationList.add(new TopCity(
                        (String) row[0],           // City Name
                        ((Number) row[1]).longValue()  // Total Flights
                ));
            }
            return destinationList;
    }
}
