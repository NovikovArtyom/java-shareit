package ru.practicum.shareit.server.exception;

public class IncorrectDateException extends IllegalArgumentException {
    public IncorrectDateException(String message) {
        super(message);
    }
}
