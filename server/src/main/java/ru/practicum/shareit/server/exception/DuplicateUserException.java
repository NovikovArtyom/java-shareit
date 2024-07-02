package ru.practicum.shareit.server.exception;

public class DuplicateUserException extends IllegalArgumentException {
    public DuplicateUserException(String message) {
        super(message);
    }
}
