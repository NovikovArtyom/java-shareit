package ru.practicum.shareit.server.exception;

public class IncorrectCommentException extends IllegalArgumentException {
    public IncorrectCommentException(String message) {
        super(message);
    }
}
