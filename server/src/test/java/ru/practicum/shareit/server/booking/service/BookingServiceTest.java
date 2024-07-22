package ru.practicum.shareit.server.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.model.BookingEntity;
import ru.practicum.shareit.server.booking.model.BookingStatus;
import ru.practicum.shareit.server.exception.UserAccessException;
import ru.practicum.shareit.server.item.model.ItemEntity;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.user.model.UserEntity;
import ru.practicum.shareit.server.user.service.UserService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
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
    void getBookingByIdSuccess() {
        BookingEntity receivedBooking = bookingService.getBookingById(savedBooker.getId(), booking.getId());
        verifyBooking(receivedBooking, savedBooking);
    }

    @Test
    void getBookingByIdUserAccessException() {
        assertThatThrownBy(() -> bookingService.getBookingById(999L, savedBooking.getId()))
                .isInstanceOf(UserAccessException.class)
                .hasMessageContaining("Просматривать данные о бронировании может только владелец вещи или автор брони!");
    }

    @Test
    void getBookingsByBookerSuccess() {
        List<BookingEntity> receivedBooking = bookingService.getBookingsByBooker(savedBooker.getId(), "WAITING", 0, 10);
        assertThat(receivedBooking).hasSize(1);
        verifyBooking(receivedBooking.get(0), savedBooking);
    }

    @Test
    void getBookingsByOwnerSuccess() {
        List<BookingEntity> receivedBooking = bookingService.getBookingByOwner(savedItemOwner.getId(), "WAITING", 0, 10);
        assertThat(receivedBooking).hasSize(1);
        verifyBooking(receivedBooking.get(0), savedBooking);
    }

    @Test
    void approveBookingSuccess() {
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
