package ru.practicum.shareit.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ShortErrorResponse handle(final IncorrectStatusException e) {
        String state = e.getState();
        log.error("Указан некорректный статус!");
        return new ShortErrorResponse(String.format("Unknown state: %s", state));
    }
}