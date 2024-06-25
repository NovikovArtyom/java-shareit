package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.user.model.UserEntity;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EntityManager em;

    private UserEntity user;
    private ItemEntity item;
    private BookingEntity booking;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setName("Артём");
        user.setEmail("artyom@gmail.com");
        em.persist(user);

        item = new ItemEntity();
        item.setName("Дрель");
        item.setDescription("Дрель Макито");
        item.setAvailable(true);
        item.setOwner(user);
        em.persist(item);

        booking = new BookingEntity();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        em.persist(booking);
    }

    @Test
    void testFindAllByBooker_Id() {
        bookingRepository.save(booking);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookingEntity> bookings = bookingRepository.findAllByBooker_Id(user.getId(), pageable);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getContent().get(0)).isEqualTo(booking);
    }

    @Test
    void testFindAllByBooker_IdAndStartBeforeAndEndAfter() {
        bookingRepository.save(booking);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookingEntity> bookings = bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(user.getId(),
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), pageable);

        assertThat(bookings).hasSize(0);
    }

    @Test
    void testFindAllByBooker_IdAndStartAfter() {
        bookingRepository.save(booking);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookingEntity> bookings = bookingRepository.findAllByBooker_IdAndStartAfter(user.getId(), LocalDateTime.now(), pageable);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getContent().get(0)).isEqualTo(booking);
    }

    @Test
    void testFindAllByBooker_IdAndEndBefore() {
        bookingRepository.save(booking);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookingEntity> bookings = bookingRepository.findAllByBooker_IdAndEndBefore(user.getId(), LocalDateTime.now().plusDays(3), pageable);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getContent().get(0)).isEqualTo(booking);
    }

    @Test
    void testFindAllByBooker_IdAndStatusEquals() {
        bookingRepository.save(booking);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookingEntity> bookings = bookingRepository.findAllByBooker_IdAndStatusEquals(user.getId(), BookingStatus.WAITING, pageable);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getContent().get(0)).isEqualTo(booking);
    }

    @Test
    void testFindAllByItem_Owner_IdOrderByStartDesc() {
        bookingRepository.save(booking);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookingEntity> bookings = bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(user.getId(), pageable);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getContent().get(0)).isEqualTo(booking);
    }

    @Test
    void testFindAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc() {
        bookingRepository.save(booking);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookingEntity> bookings = bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(user.getId(),
                LocalDateTime.now().plusDays(1).plusHours(1), LocalDateTime.now().plusDays(1).plusHours(1), pageable);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getContent().get(0)).isEqualTo(booking);
    }

    @Test
    void testFindAllByItem_Owner_IdAndStartAfterOrderByStartDesc() {
        bookingRepository.save(booking);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookingEntity> bookings = bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(user.getId(), LocalDateTime.now(), pageable);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getContent().get(0)).isEqualTo(booking);
    }

    @Test
    void testFindAllByItem_Owner_IdAndEndBeforeOrderByStartDesc() {
        bookingRepository.save(booking);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookingEntity> bookings = bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(user.getId(),
                LocalDateTime.now().plusDays(3), pageable);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getContent().get(0)).isEqualTo(booking);
    }

    @Test
    void testFindAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc() {
        bookingRepository.save(booking);
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookingEntity> bookings = bookingRepository.findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(user.getId(), BookingStatus.WAITING, pageable);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.getContent().get(0)).isEqualTo(booking);
    }

    @Test
    void testFindTop1ByItem_IdAndStartBeforeAndStatusEqualsOrderByStartDesc() {
        bookingRepository.save(booking);
        BookingEntity result = bookingRepository.findTop1ByItem_IdAndStartBeforeAndStatusEqualsOrderByStartDesc(item.getId(), LocalDateTime.now().plusDays(1), BookingStatus.WAITING);

        assertThat(result).isEqualTo(booking);
    }

    @Test
    void testFindTop1ByItem_IdAndStartAfterAndStatusEqualsOrderByStart() {
        bookingRepository.save(booking);
        BookingEntity result = bookingRepository.findTop1ByItem_IdAndStartAfterAndStatusEqualsOrderByStart(item.getId(), LocalDateTime.now(), BookingStatus.WAITING);

        assertThat(result).isEqualTo(booking);
    }

    @Test
    void testExistsByItem_IdAndBooker_IdAndStatusEqualsAndStartBefore() {
        bookingRepository.save(booking);
        Boolean exists = bookingRepository.existsByItem_IdAndBooker_IdAndStatusEqualsAndStartBefore(item.getId(),
                user.getId(), BookingStatus.WAITING, LocalDateTime.now().plusDays(3));
        assertThat(exists).isTrue();
    }
}
