package io.vitech.flights.tracker.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vitech.flights.tracker.service.SyncService;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class SyncController {

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @Operation(summary = "Trigger a job to sync data", description = "This endpoint triggers a job to synchronize data in the platform.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Sync job accepted and processing"),
            @ApiResponse(responseCode = "500", description = "Error occurred while triggering the sync job")
    })
    @PostMapping("/sync")
    public ResponseEntity<?> triggerSyncJob() {
        try {
            // Trigger the sync job
            syncService.syncData();

            // Returning a 202 Accepted response with success message
            SyncResponse syncResponse = new SyncResponse();
            syncResponse.setMessage("Sync job successfully triggered.");
            syncResponse.setStatus("accepted");

            return new ResponseEntity<>(syncResponse, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            // In case of an error, return a 500 Internal Server Error
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError("Failed to trigger sync job.");
            errorResponse.setDetails(e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class SyncResponse {
        @JsonProperty("message")
        private String message;

        @JsonProperty("status")
        private String status;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @RequiredArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class ErrorResponse {
        @JsonProperty("error")
        private String error;

        @JsonProperty("details")
        private String details;

    }
}
