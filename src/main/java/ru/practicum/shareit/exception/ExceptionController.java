package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final DuplicateUserException e) {
        log.error("Данный пользователь уже зарегистрирован!");
        return new ErrorResponse("error: ", "Данный пользователь уже зарегистрирован!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final UserNotFoundException e) {
        log.error("Данный пользователь не зарегистрирован!");
        return new ErrorResponse("error: ", "Данный пользователь не зарегистрирован!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final DuplicateEmailException e) {
        log.error("Пользователь с данным email уже зарегистрирован!");
        return new ErrorResponse("error: ", "Пользователь с данным email уже зарегистрирован!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final UserAccessException e) {
        log.error("Проблема с доступом к выполняемому действию!");
        return new ErrorResponse("error: ", "Проблема с доступом к выполняемому действию!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final ItemNotFoundException e) {
        log.error("Данная вещь не зарегистрирована!");
        return new ErrorResponse("error: ", "Данная вещь не зарегистрирована!");
    }
}
