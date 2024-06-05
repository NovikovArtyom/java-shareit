package ru.practicum.shareit.exception;

public class IncorrectStatusException extends IllegalArgumentException {
    public IncorrectStatusException(String message) {
        super(message);
    }
}
