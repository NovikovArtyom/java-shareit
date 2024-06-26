package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.exception.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ActiveProfiles("test")
public class ExceptionsTest {

    @Test
    void testBookingNotFoundException() {
        String message = "Данная запись о бронировании не зарегистрирована!";
        BookingNotFoundException exception = new BookingNotFoundException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testDuplicateEmailException() {
        String message = "Пользователь с данным email уже зарегистрирован!";
        DuplicateEmailException exception = new DuplicateEmailException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testDuplicateItemException() {
        String message = "Указанный предмет уже зарегистрирован!";
        DuplicateItemException exception = new DuplicateItemException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testDuplicateUserException() {
        String message = "Данный пользователь уже зарегистрирован!";
        DuplicateUserException exception = new DuplicateUserException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testIncorrectCommentException() {
        String message = "Добавление комментария невозможно!";
        IncorrectCommentException exception = new IncorrectCommentException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testIncorrectDateException() {
        String message = "Дата старта аренды не может быть позднее даты конца аренды!";
        IncorrectDateException exception = new IncorrectDateException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testIncorrectStatusException() {
        String state = "INVALID_STATE";
        IncorrectStatusException exception = new IncorrectStatusException(state);

        ExceptionController controller = new ExceptionController();
        ShortErrorResponse response = controller.handle(exception);

        assertEquals(String.format("Unknown state: %s", state), response.getError());
    }

    @Test
    void testItemNotAvailableException() {
        String message = "Данный товар уже забронирован!";
        ItemNotAvailableException exception = new ItemNotAvailableException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testItemNotFoundException() {
        String message = "Данная вещь не зарегистрирована!";
        ItemNotFoundException exception = new ItemNotFoundException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testItemRequestNotFoundException() {
        String message = "Информация о заявке не найдена!";
        ItemRequestNotFoundException exception = new ItemRequestNotFoundException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testRepeatedApproveException() {
        String message = "Подтвердить бронирование можно только оно в статусе WAITING!";
        RepeatedApproveException exception = new RepeatedApproveException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testUserAccessException() {
        String message = "Проблема с доступом к выполняемому действию!";
        UserAccessException exception = new UserAccessException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testUserNotFoundException() {
        String message = "Данный пользователь не зарегистрирован!";
        UserNotFoundException exception = new UserNotFoundException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }

    @Test
    void testIncorrectStateException() {
        String message = "Указаный параметр state некорректен!";
        IncorrectStateException exception = new IncorrectStateException(message);

        ExceptionController controller = new ExceptionController();
        ErrorResponse response = controller.handle(exception);

        assertEquals("error: ", response.getError());
        assertEquals(message, response.getDescription());
    }
}
