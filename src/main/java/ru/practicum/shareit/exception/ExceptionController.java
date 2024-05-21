package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final DuplicateUserException e) {
        return new ErrorResponse("error: ", "Данный пользователь уже зарегистрирован");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final UserNotFoundException e) {
        return new ErrorResponse("error: ", "Данный пользователь не зарегистрирован");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final DuplicateEmailException e) {
        return new ErrorResponse("error: ", "Пользователь с данным email уже зарегистрирован");
    }
}
