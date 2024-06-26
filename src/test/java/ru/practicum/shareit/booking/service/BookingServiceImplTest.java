package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Long bookerId = 1L;
    private Long ownerId = 2L;
    private Long itemId = 1L;
    private Integer from = 0;
    private Integer size = 10;

    @BeforeEach
    public void setUp() {
        UserEntity booker = new UserEntity(bookerId, "Артём", "artyom@mail.ru");
        UserEntity owner = new UserEntity(ownerId, "Владимир", "vladimir@mail.ru");
        lenient().when(userService.getUserById(bookerId)).thenReturn(booker);
        lenient().when(userService.getUserById(ownerId)).thenReturn(owner);
    }

    @Test
    public void testGetBookingsByBooker_All() {
        String state = "ALL";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();
        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.minusDays(1), now.plusDays(1), null, null, BookingStatus.WAITING),
                new BookingEntity(2L, now.minusDays(2), now.plusDays(2), null, null, BookingStatus.APPROVED)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        when(bookingRepository.findAllByBooker_Id(eq(bookerId), eq(pageable))).thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingsByBooker(bookerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingsByBooker_Current() {
        String state = "CURRENT";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();
        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.minusDays(1), now.plusDays(1), null, null, BookingStatus.WAITING),
                new BookingEntity(2L, now.minusDays(2), now.plusDays(2), null, null, BookingStatus.APPROVED)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        when(bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(eq(bookerId), any(LocalDateTime.class), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingsByBooker(bookerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingsByBooker_Past() {
        String state = "PAST";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();
        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.minusDays(2), now.minusDays(1), null, null, BookingStatus.APPROVED),
                new BookingEntity(2L, now.minusDays(4), now.minusDays(3), null, null, BookingStatus.CANCELED)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        when(bookingRepository.findAllByBooker_IdAndEndBefore(eq(bookerId), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingsByBooker(bookerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingsByBooker_Future() {
        String state = "FUTURE";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();
        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.plusDays(1), now.plusDays(2), null, null, BookingStatus.WAITING),
                new BookingEntity(2L, now.plusDays(3), now.plusDays(4), null, null, BookingStatus.APPROVED)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        when(bookingRepository.findAllByBooker_IdAndStartAfter(eq(bookerId), any(LocalDateTime.class), eq(pageable)))
                .thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingsByBooker(bookerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingsByBooker_Waiting() {
        String state = "WAITING";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();
        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.plusDays(1), now.plusDays(2), null, null, BookingStatus.WAITING),
                new BookingEntity(2L, now.plusDays(3), now.plusDays(4), null, null, BookingStatus.WAITING)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        when(bookingRepository.findAllByBooker_IdAndStatusEquals(eq(bookerId), eq(BookingStatus.WAITING), eq(pageable)))
                .thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingsByBooker(bookerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingsByBooker_Rejected() {
        String state = "REJECTED";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();
        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.plusDays(1), now.plusDays(2), null, null, BookingStatus.REJECTED),
                new BookingEntity(2L, now.plusDays(3), now.plusDays(4), null, null, BookingStatus.REJECTED)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        when(bookingRepository.findAllByBooker_IdAndStatusEquals(eq(bookerId), eq(BookingStatus.REJECTED), eq(pageable)))
                .thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingsByBooker(bookerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingsByBooker_UserNotFound() {
        when(userService.getUserById(bookerId)).thenThrow(new UserNotFoundException("User not found"));

        Exception exception = assertThrows(UserNotFoundException.class, () ->
                bookingService.getBookingsByBooker(bookerId, "ALL", from, size));

        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    public void testGetBookingByOwner_All() {
        String state = "ALL";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();

        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.minusDays(1), now.plusDays(1), null, null, BookingStatus.WAITING),
                new BookingEntity(2L, now.minusDays(2), now.plusDays(2), null, null, BookingStatus.APPROVED)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        lenient().when(bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(any(), any())).thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingByOwner(ownerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingByOwner_Current() {
        String state = "CURRENT";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();

        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.minusDays(1), now.plusDays(1), null, null, BookingStatus.WAITING),
                new BookingEntity(2L, now.minusDays(2), now.plusDays(2), null, null, BookingStatus.APPROVED)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        lenient().when(bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(any(),
                        any(LocalDateTime.class), any(LocalDateTime.class), any()))
                .thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingByOwner(ownerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingByOwner_Past() {
        String state = "PAST";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();
        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.minusDays(2), now.minusDays(1), null, null, BookingStatus.APPROVED),
                new BookingEntity(2L, now.minusDays(4), now.minusDays(3), null, null, BookingStatus.CANCELED)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        lenient().when(bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(any(), any(LocalDateTime.class), any()))
                .thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingByOwner(ownerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingByOwner_Future() {
        String state = "FUTURE";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();
        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.plusDays(1), now.plusDays(2), null, null, BookingStatus.WAITING),
                new BookingEntity(2L, now.plusDays(3), now.plusDays(4), null, null, BookingStatus.APPROVED)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        lenient().when(bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(any(), any(LocalDateTime.class), any()))
                .thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingByOwner(ownerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingByOwner_Waiting() {
        String state = "WAITING";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();
        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.plusDays(1), now.plusDays(2), null, null, BookingStatus.WAITING),
                new BookingEntity(2L, now.plusDays(3), now.plusDays(4), null, null, BookingStatus.WAITING)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        lenient().when(bookingRepository.findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(any(), eq(BookingStatus.WAITING), any()))
                .thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingByOwner(ownerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingByOwner_Rejected() {
        String state = "REJECTED";
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        LocalDateTime now = LocalDateTime.now();

        List<BookingEntity> bookings = Arrays.asList(
                new BookingEntity(1L, now.plusDays(1), now.plusDays(2), null, null, BookingStatus.REJECTED),
                new BookingEntity(2L, now.plusDays(3), now.plusDays(4), null, null, BookingStatus.REJECTED)
        );
        PageImpl<BookingEntity> page = new PageImpl<>(bookings);

        lenient().when(bookingRepository.findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(any(), eq(BookingStatus.REJECTED), any()))
                .thenReturn(page);

        List<BookingEntity> result = bookingService.getBookingByOwner(ownerId, state, from, size);

        assertThat(result).isEqualTo(bookings);
    }

    @Test
    public void testGetBookingByOwner_UserNotFound() {
        when(userService.getUserById(bookerId)).thenThrow(new UserNotFoundException("User not found"));

        Exception exception = assertThrows(UserNotFoundException.class, () ->
                bookingService.getBookingByOwner(bookerId, "ALL", from, size));

        assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    public void testGetBookingById_BookingNotFound() {
        when(bookingRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookingNotFoundException.class, () ->
                bookingService.getBookingById(1L, 1L));

        assertThat(exception.getMessage()).isEqualTo("Бронь по заданному ID не найдена");
    }

    @Test
    public void testApproveBooking_BookingNotFound() {
        when(bookingRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookingNotFoundException.class, () ->
                bookingService.approveBooking(1L, 1L, true));

        assertThat(exception.getMessage()).isEqualTo("Заявка о бронировании по дайнному Id не найдена!");
    }
}
