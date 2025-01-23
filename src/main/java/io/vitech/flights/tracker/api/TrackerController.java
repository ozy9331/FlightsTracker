package io.vitech.flights.tracker.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackerController {


    @GetMapping("/")
    public ResponseEntity<String> helloGet() {

        return ResponseEntity.ok("Opa");
    }

}
