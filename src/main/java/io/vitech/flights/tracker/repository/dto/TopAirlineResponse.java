package io.vitech.flights.tracker.repository.dto;

public record TopAirlineResponse(String airlineName, String iataCode, long totalFlights) {}
