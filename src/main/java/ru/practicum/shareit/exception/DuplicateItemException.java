package ru.practicum.shareit.exception;

public class DuplicateItemException extends IllegalArgumentException {
    public DuplicateItemException (String message) {
        super(message);
    }
}
