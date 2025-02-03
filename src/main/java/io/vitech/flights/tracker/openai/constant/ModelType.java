package io.vitech.flights.tracker.openai.constant;

public enum ModelType {
    GPT_4O_MINI("gpt-4o-mini");

    private final String value;

    ModelType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
