package ru.practicum.shareit.server.exception;

public class IncorrectStateException extends IllegalArgumentException {
    public IncorrectStateException(String message) {
        super(message);
    }
}
