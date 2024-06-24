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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final IncorrectDateException e) {
        log.error("Дата старта аренды не может быть позднее даты конца аренды!");
        return new ErrorResponse("error: ", "Дата старта аренды не может быть позднее даты конца аренды!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final BookingNotFoundException e) {
        log.error("Данная запись о бронировании не зарегистрирована!");
        return new ErrorResponse("error: ", "Данная запись о бронировании не зарегистрирована!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final IncorrectStateException e) {
        log.error("Указаный параметр state некорректен!");
        return new ErrorResponse("error: ", "Указаный параметр state некорректен!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ItemNotAvailableException e) {
        log.error("Данный товар уже забронирован!");
        return new ErrorResponse("error: ", "Данный товар уже забронирован!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final RepeatedApproveException e) {
        log.error("Подтвердить бронирование можно только оно в статусе WAITING!");
        return new ErrorResponse("error: ", "Подтвердить бронирование можно только оно в статусе WAITING!");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ShortErrorResponse handle(final IncorrectStatusException e) {
        String state = e.getState();
        log.error("Указан некорректный статус!");
        return new ShortErrorResponse(String.format("Unknown state: %s", state));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final IncorrectCommentException e) {
        log.error("Добавление комментария невозможно!");
        return new ErrorResponse("error: ", "Добавление комментария невозможно!");
    }


}
