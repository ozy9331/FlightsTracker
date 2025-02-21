package io.vitech.flights.tracker.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vitech.flights.tracker.conf.PaginationConfig;
import io.vitech.flights.tracker.entity.AircraftEntity;
import io.vitech.flights.tracker.entity.AirlineEntity;
import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.CityEntity;
import io.vitech.flights.tracker.entity.FlightEntity;
import io.vitech.flights.tracker.exception.ErrorResponse;
import io.vitech.flights.tracker.openai.AIAnalysisService;
import io.vitech.flights.tracker.service.AircraftService;
import io.vitech.flights.tracker.service.AirlineService;
import io.vitech.flights.tracker.service.AirportService;
import io.vitech.flights.tracker.service.CityService;
import io.vitech.flights.tracker.service.FlightService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final AIAnalysisService aiAnalysisService;

    public FlightTrackerController(AirportService airportService, FlightService flightService,
                                   CityService cityService, AirlineService airlineService,
                                   AircraftService aircraftService, PaginationConfig paginationConfig, AIAnalysisService aiAnalysisService) {
        this.airportService = airportService;
        this.flightService = flightService;
        this.cityService = cityService;
        this.airlineService = airlineService;
        this.aircraftService = aircraftService;
        this.paginationConfig = paginationConfig;
        this.aiAnalysisService = aiAnalysisService;
    }

    @Operation(summary = "Get all flights with pagination")
    @GetMapping("/flights")
    public ResponseEntity<Page<FlightEntity>> getAllFlights(Pageable pageable, @RequestParam(required = false) Integer size,
                                                            @RequestParam(required = false) Integer rangeStart,
                                                            @RequestParam(required = false) Integer rangeEnd) {
        return ResponseEntity.ok(flightService.getAllFlights(PageRequest.of(pageable.getPageNumber(), getPageSize(size)), rangeStart, rangeEnd));
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
    public ResponseEntity<Page<AirportEntity>> getAllAirports (Pageable pageable,
                                                               @RequestParam(required = false) Integer size,
                                                               @RequestParam(required = false) String name,
                                                               @RequestParam(required = false) String city) {

        return ResponseEntity.ok(airportService.getAllAirports(PageRequest.of(pageable.getPageNumber(), getPageSize(size)), name, city));
    }

    @Operation(summary = "Get an airport by ID")
    @GetMapping("/airports/{id}")
    public ResponseEntity<AirportEntity> getAirportById(@PathVariable int id) {
        return ResponseEntity.ok(airportService.getAirportById(id));
    }

    @Operation(summary = "Get all cities")
    @GetMapping("/cities")
    public ResponseEntity<Page<CityEntity>> getAllCities(Pageable pageable,
                                                         @RequestParam(required = false) Integer size,
                                                         @RequestParam(required = false) String name) {

        return ResponseEntity.ok(cityService.getAllCities(PageRequest.of(pageable.getPageNumber(), getPageSize(size)), name));
    }

    @Operation(summary = "Get a city by ID")
    @GetMapping("/cities/{id}")
    public ResponseEntity<CityEntity> getCityById(@PathVariable int id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }


    @Operation(summary = "Get all airlines")
    @GetMapping("/airlines")
    public ResponseEntity<Page<AirlineEntity>> getAllAirlines(Pageable pageable,
                                                              @RequestParam(required = false) Integer size,
                                                              @RequestParam(required = false) String name) {

        return ResponseEntity.ok(airlineService.getAllAirlines(PageRequest.of(pageable.getPageNumber(), getPageSize(size)), name));
    }

    @Operation(summary = "Get an airline by ID")
    @GetMapping("/airlines/{id}")
    public ResponseEntity<AirlineEntity> getAirlineById(@PathVariable int id) {
        return ResponseEntity.ok(airlineService.getAirlineById(id));
    }

    @Operation(summary = "Get all aircrafts")
    @GetMapping("/aircrafts")
    public ResponseEntity<Page<AircraftEntity>> getAllAircrafts(Pageable pageable,
                                                                @RequestParam(required = false) Integer size) {
        return ResponseEntity.ok(aircraftService.getAllAircrafts(PageRequest.of(pageable.getPageNumber(), getPageSize(size))));
    }

    @Operation(summary = "Get an aircraft by ID")
    @GetMapping("/aircrafts/{id}")
    public ResponseEntity<AircraftEntity> getAircraftById(@PathVariable int id) {
        return ResponseEntity.ok(aircraftService.getAircraftById(id));
    }

    @Operation(summary = "Get top entities based on type")
    @GetMapping("/top")
    public ResponseEntity<?> getTopEntities(@Parameter(in = ParameterIn.QUERY,
                                                       description = "Type of top entities to retrieve",
                                                       schema = @Schema(allowableValues = {"destinations", "busiest-day", "airlines", "aircrafts", "cities"}))
                                            @RequestParam String type,
                                            @RequestParam(required = false) Integer limit,
                                            @RequestParam(required = false) LocalDate startDate,
                                            @RequestParam(required = false) LocalDate endDate,
                                            @RequestParam(required = false) Double rangeStart,
                                            @RequestParam(required = false) Double rangeEnd,
                                            @RequestParam(required = false) String aircraftType,
                                            @RequestParam(required = false) String city,
                                            @RequestParam(required = false) String airport) {

        int pageSize = getPageSize(limit);

        return switch (type) {
            case "destinations" ->
                    ResponseEntity.ok(flightService.getTopDestinations(pageSize, rangeStart, rangeEnd, startDate, endDate, city, airport));
            case "busiest-day" ->
                    ResponseEntity.ok(flightService.getBusiestDays(rangeStart, rangeEnd, startDate, endDate, city, airport, limit));
            case "airlines" ->
                    ResponseEntity.ok(flightService.findTopAirlines(rangeStart, rangeEnd, pageSize, startDate, endDate, city, airport));
            case "aircrafts" ->
                    ResponseEntity.ok(flightService.findTopAircrafts(rangeStart, rangeEnd, pageSize, startDate, endDate, city, airport));
            case "cities" ->
                    ResponseEntity.ok(flightService.findTopDestinationCities(rangeStart, rangeEnd, pageSize , aircraftType, startDate, endDate));

            default -> ResponseEntity.badRequest().body( new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Invalid value for parameter '[type]'."
            ));
        };
    }

    @Operation(summary = "Get an aircraft by ID")
    @GetMapping("/ai/top-statistics")
    public ResponseEntity<?> analyze(@RequestParam(required = false) LocalDate startDate,
                                     @RequestParam(required = false) LocalDate endDate,
                                     @RequestParam(required = false) Double rangeStart,
                                     @RequestParam(required = false) Double rangeEnd) throws JsonProcessingException {

        return ResponseEntity.ok(aiAnalysisService.getTopStatistics(rangeStart, rangeEnd, startDate, endDate));
    }

    private int getPageSize(Integer size) {
        return Objects.nonNull(size) && size > 0 ? Math.min(size, paginationConfig.getMaxPageSize()) : paginationConfig.getDefaultPageSize();
    }
}
