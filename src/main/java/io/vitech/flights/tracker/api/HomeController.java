package io.vitech.flights.tracker.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class HomeController {

    // Mapping for the home endpoint
    @GetMapping("/")
    public ResponseEntity<String> home() {
        String welcomeMessage = "Welcome to the Flights Tracker API!";
        return ResponseEntity.ok(welcomeMessage);
    }

}
