package io.vitech.flights.tracker.openai.constant;

public enum RoleType {
    USER("user"),
    SYSTEM("system"),
    ASSISTANT("assistant");

    private final String value;

    RoleType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
