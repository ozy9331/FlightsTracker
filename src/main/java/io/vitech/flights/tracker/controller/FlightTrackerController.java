package io.vitech.flights.tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vitech.flights.tracker.conf.PaginationConfig;
import io.vitech.flights.tracker.entity.AircraftEntity;
import io.vitech.flights.tracker.entity.AirlineEntity;
import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.CityEntity;
import io.vitech.flights.tracker.entity.FlightEntity;
import io.vitech.flights.tracker.service.AircraftService;
import io.vitech.flights.tracker.service.AirlineService;
import io.vitech.flights.tracker.service.AirportService;
import io.vitech.flights.tracker.service.CityService;
import io.vitech.flights.tracker.service.FlightService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class FlightTrackerController {

    private final PaginationConfig paginationConfig;
    private final AirportService airportService;
    private final FlightService flightService;
    private final CityService cityService;
    private final AirlineService airlineService;
    private final AircraftService aircraftService;

    public FlightTrackerController(AirportService airportService, FlightService flightService,
                                   CityService cityService, AirlineService airlineService,
                                   AircraftService aircraftService, PaginationConfig paginationConfig) {
        this.airportService = airportService;
        this.flightService = flightService;
        this.cityService = cityService;
        this.airlineService = airlineService;
        this.aircraftService = aircraftService;
        this.paginationConfig = paginationConfig;
    }

    @Operation(summary = "Get all flights with pagination")
    @GetMapping("/flights")
    public ResponseEntity<Page<FlightEntity>> getAllFlights(Pageable pageable, @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(flightService.getAllFlights(PageRequest.of(pageable.getPageNumber(), getPageSize(size))));
    }

    @Operation(summary = "Get a flight by ID")
    @GetMapping("/flights/{id}")
    public ResponseEntity<FlightEntity> getFlightById(@PathVariable int id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    @Operation(summary = "Get all airports")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of flights retrieved successfully")
    })
    @GetMapping("/airports")
    public ResponseEntity<Page<AirportEntity>> getAllAirports (Pageable pageable, @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(airportService.getAllAirports(PageRequest.of(pageable.getPageNumber(), getPageSize(size))));
    }

    @Operation(summary = "Get an airport by ID")
    @GetMapping("/airports/{id}")
    public ResponseEntity<AirportEntity> getAirportById(@PathVariable int id) {
        return ResponseEntity.ok(airportService.getAirportById(id));
    }

    @Operation(summary = "Get all cities")
    @GetMapping("/cities")
    public ResponseEntity<Page<CityEntity>> getAllCities(Pageable pageable, @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(cityService.getAllCities(PageRequest.of(pageable.getPageNumber(), getPageSize(size))));
    }

    @Operation(summary = "Get a city by ID")
    @GetMapping("/cities/{id}")
    public ResponseEntity<CityEntity> getCityById(@PathVariable int id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }


    @Operation(summary = "Get all airlines")
    @GetMapping("/airlines")
    public ResponseEntity<Page<AirlineEntity>> getAllAirlines(Pageable pageable, @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(airlineService.getAllAirlines(PageRequest.of(pageable.getPageNumber(), getPageSize(size))));
    }

    @Operation(summary = "Get an airline by ID")
    @GetMapping("/airlines/{id}")
    public ResponseEntity<AirlineEntity> getAirlineById(@PathVariable int id) {
        return ResponseEntity.ok(airlineService.getAirlineById(id));
    }

    @Operation(summary = "Get all aircrafts")
    @GetMapping("/aircrafts")
    public ResponseEntity<Page<AircraftEntity>> getAllAircrafts(Pageable pageable, @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(aircraftService.getAllAircrafts(PageRequest.of(pageable.getPageNumber(), getPageSize(size))));
    }

    @Operation(summary = "Get an aircraft by ID")
    @GetMapping("/aircrafts/{id}")
    public ResponseEntity<AircraftEntity> getAircraftById(@PathVariable int id) {
        return ResponseEntity.ok(aircraftService.getAircraftById(id));
    }

    private int getPageSize(Integer size) {
        return Objects.nonNull(size) && size > 0 ? Math.min(size, paginationConfig.getMaxPageSize()) : paginationConfig.getDefaultPageSize();
    }
}
