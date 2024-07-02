package ru.practicum.shareit.server.exception;

public class ItemNotAvailableException extends IllegalArgumentException {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}

