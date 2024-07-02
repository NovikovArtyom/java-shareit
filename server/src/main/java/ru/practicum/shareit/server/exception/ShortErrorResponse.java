package ru.practicum.shareit.server.exception;

public class ShortErrorResponse {
    private String error;

    public ShortErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
