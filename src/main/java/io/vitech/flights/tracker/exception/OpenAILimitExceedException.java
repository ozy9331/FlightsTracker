package io.vitech.flights.tracker.exception;

public class OpenAILimitExceedException extends RuntimeException {
    public OpenAILimitExceedException(String msg) {
        super(msg);
    }
}
