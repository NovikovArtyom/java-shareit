package ru.practicum.shareit.server.exception;

public class ItemNotFoundException extends IllegalArgumentException {
    public ItemNotFoundException(String s) {
        super(s);
    }
}
