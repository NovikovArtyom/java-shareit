package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.UserAccessException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import static org.assertj.core.api.Assertions.assertThat;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private final EntityManager em;

    private UserEntity itemOwner;
    private UserEntity savedItemOwner;
    private UserEntity booker;
    private UserEntity savedBooker;
    private ItemEntity item;
    private ItemEntity savedItem;
    BookingEntity booking;
    BookingEntity savedBooking;

    @BeforeEach
    void setUp() {
        itemOwner = new UserEntity(null, "Артём", "artyom@gmail.com");
        savedItemOwner = userService.createUser(itemOwner);
        booker = new UserEntity(null, "Владимир", "vladimir@mail.ru");
        savedBooker = userService.createUser(booker);
        item = new ItemEntity(null, "Дрель", "Дрель Мокито", true, null, null);
        savedItem = itemService.addItem(savedItemOwner.getId(), null, item);
        booking = new BookingEntity(null, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                null, null, null);
        savedBooking = bookingService.addBooking(savedBooker.getId(), booking, savedItem.getId());
    }

    @Test
    void testGetBookingById() {
        BookingEntity receivedBooking = bookingService.getBookingById(savedBooker.getId(), booking.getId());
        verifyBooking(receivedBooking, savedBooking);
    }

    @Test
    void testGetBookingByIdWithIncorrectUser() {
        assertThatThrownBy(() -> bookingService.getBookingById(999L, savedBooking.getId()))
                .isInstanceOf(UserAccessException.class)
                .hasMessageContaining("Просматривать данные о бронировании может только владелец вещи или автор брони!");
    }

    @Test
    void testGetBookingsByBooker() {
        List<BookingEntity> receivedBooking = bookingService.getBookingsByBooker(savedBooker.getId(), "WAITING", 0, 10);
        assertThat(receivedBooking).hasSize(1);
        verifyBooking(receivedBooking.get(0), savedBooking);
    }

    @Test
    void testGetBookingsByOwner() {
        List<BookingEntity> receivedBooking = bookingService.getBookingByOwner(savedItemOwner.getId(), "WAITING", 0, 10);
        assertThat(receivedBooking).hasSize(1);
        verifyBooking(receivedBooking.get(0), savedBooking);
    }

    @Test
    void testApproveBooking() {
        BookingEntity receivedBooking = bookingService.approveBooking(savedItemOwner.getId(), savedBooking.getId(), true);
        assertThat(receivedBooking.getId(), notNullValue());
        assertThat(receivedBooking.getStart(), equalTo(savedBooking.getStart()));
        assertThat(receivedBooking.getEnd(), equalTo(savedBooking.getEnd()));
        assertThat(receivedBooking.getItem(), equalTo(savedBooking.getItem()));
        assertThat(receivedBooking.getBooker(), equalTo(savedBooking.getBooker()));
        assertThat(receivedBooking.getStatus(), equalTo(BookingStatus.APPROVED));
    }

    private void verifyBooking(BookingEntity received, BookingEntity expected) {
        assertThat(received.getId(), notNullValue());
        assertThat(received.getStart(), equalTo(expected.getStart()));
        assertThat(received.getEnd(), equalTo(expected.getEnd()));
        assertThat(received.getItem(), equalTo(expected.getItem()));
        assertThat(received.getBooker(), equalTo(expected.getBooker()));
        assertThat(received.getStatus(), equalTo(expected.getStatus()));
    }
}
