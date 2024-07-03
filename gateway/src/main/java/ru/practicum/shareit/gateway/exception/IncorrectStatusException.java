package ru.practicum.shareit.gateway.exception;

public class IncorrectStatusException extends IllegalArgumentException {
    private final String state;

    public IncorrectStatusException(String state) {
        super(String.format("Unknown state '%s'", state));
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
