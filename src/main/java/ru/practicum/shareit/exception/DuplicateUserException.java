package ru.practicum.shareit.exception;

public class DuplicateUserException extends IllegalArgumentException {
    public DuplicateUserException (String message) {
        super(message);
    }
}
