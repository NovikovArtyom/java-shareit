package ru.practicum.shareit.exception;

public class RepeatedApproveException extends IllegalArgumentException {
    public RepeatedApproveException(String s) {
        super(s);
    }
}
