package ru.practicum.shareit.server.exception;

public class UserAccessException extends IllegalAccessError {
    public UserAccessException(String message) {
        super(message);
    }
}
