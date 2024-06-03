package ru.practicum.shareit.exception;

public class UserAccessException extends IllegalAccessError {
    public UserAccessException(String message) {
        super(message);
    }
}
