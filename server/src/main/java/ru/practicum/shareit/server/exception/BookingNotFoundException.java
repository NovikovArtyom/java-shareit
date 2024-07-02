package ru.practicum.shareit.server.exception;

public class BookingNotFoundException extends IllegalArgumentException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
