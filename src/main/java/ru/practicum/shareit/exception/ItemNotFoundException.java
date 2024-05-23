package ru.practicum.shareit.exception;

public class ItemNotFoundException extends IllegalArgumentException {
    public ItemNotFoundException(String s) {
        super(s);
    }
}
