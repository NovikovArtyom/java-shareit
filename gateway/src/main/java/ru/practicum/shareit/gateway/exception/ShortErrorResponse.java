package ru.practicum.shareit.gateway.exception;

public class ShortErrorResponse {
    private String error;

    public ShortErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
