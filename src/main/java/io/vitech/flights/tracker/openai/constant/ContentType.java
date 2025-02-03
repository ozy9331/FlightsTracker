package io.vitech.flights.tracker.openai.constant;

public enum ContentType {
    AIRPORT("gpt-4o-mini");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
