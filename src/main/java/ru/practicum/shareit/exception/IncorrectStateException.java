package ru.practicum.shareit.exception;

public class IncorrectStateException extends IllegalArgumentException {
    public IncorrectStateException(String message) {
        super(message);
    }
}
