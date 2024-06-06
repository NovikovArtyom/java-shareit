package ru.practicum.shareit.exception;

public class IncorrectCommentException extends IllegalArgumentException {
    public IncorrectCommentException(String message) {
        super(message);
    }
}
