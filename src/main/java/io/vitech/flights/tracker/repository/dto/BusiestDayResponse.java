package io.vitech.flights.tracker.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
public class BusiestDayResponse {
    private String dayOfWeek;
    private long totalFlights;
}
