package io.vitech.flights.tracker.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vitech.flights.tracker.service.SyncService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class SyncController

{

    private final SyncService syncService;

    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping("/sync")
    public ResponseEntity<?> triggerSyncJob() {
        try {
            // Trigger the sync job
            syncService.syncData();

            // Returning a 202 Accepted response with success message
            SyncResponse syncResponse = new SyncResponse();
            syncResponse.setMessage("Sync job successfully started.");
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
    public static class SyncResponse   {
        @JsonProperty("message")
        private String message;

        @JsonProperty("status")
        private String status;

        public SyncResponse message(String message) {
            this.message = message;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public SyncResponse status(String status) {
            this.status = status;
            return this;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SyncResponse syncResponse = (SyncResponse) o;
            return Objects.equals(this.message, syncResponse.message) &&
                    Objects.equals(this.status, syncResponse.status);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message, status);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class SyncResponse {\n");

            sb.append("    message: ").append(toIndentedString(message)).append("\n");
            sb.append("    status: ").append(toIndentedString(status)).append("\n");
            sb.append("}");
            return sb.toString();
        }

        /**
         * Convert the given object to string with each line indented by 4 spaces
         * (except the first line).
         */
        private String toIndentedString(Object o) {
            if (o == null) {
                return "null";
            }
            return o.toString().replace("\n", "\n    ");
        }
    }

    public static class ErrorResponse   {
        @JsonProperty("error")
        private String error;

        @JsonProperty("details")
        private String details;

        public ErrorResponse error(String error) {
            this.error = error;
            return this;
        }



        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public ErrorResponse details(String details) {
            this.details = details;
            return this;
        }



        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ErrorResponse errorResponse = (ErrorResponse) o;
            return Objects.equals(this.error, errorResponse.error) &&
                    Objects.equals(this.details, errorResponse.details);
        }

        @Override
        public int hashCode() {
            return Objects.hash(error, details);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("class ErrorResponse {\n");

            sb.append("    error: ").append(toIndentedString(error)).append("\n");
            sb.append("    details: ").append(toIndentedString(details)).append("\n");
            sb.append("}");
            return sb.toString();
        }

        /**
         * Convert the given object to string with each line indented by 4 spaces
         * (except the first line).
         */
        private String toIndentedString(Object o) {
            if (o == null) {
                return "null";
            }
            return o.toString().replace("\n", "\n    ");
        }
    }

}
