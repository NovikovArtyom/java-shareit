package ru.practicum.shareit.server.exception;

public class RepeatedApproveException extends IllegalArgumentException {
    public RepeatedApproveException(String s) {
        super(s);
    }
}
