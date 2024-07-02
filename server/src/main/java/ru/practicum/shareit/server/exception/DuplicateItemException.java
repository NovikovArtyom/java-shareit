package ru.practicum.shareit.server.exception;

public class DuplicateItemException extends IllegalArgumentException {
    public DuplicateItemException(String message) {
        super(message);
    }
}
