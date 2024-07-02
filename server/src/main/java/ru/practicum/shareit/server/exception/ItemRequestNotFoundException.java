package ru.practicum.shareit.server.exception;

public class ItemRequestNotFoundException extends IllegalArgumentException {
    public ItemRequestNotFoundException(String message) {
        super(message);
    }
}
