package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findAllByBooker_IdOrderByStart(Long bookerId);

    List<BookingEntity> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStart(Long booker_id, LocalDateTime start, LocalDateTime end);

    List<BookingEntity> findAllByBooker_IdAndStartAfterOrderByStart(Long booker_id, LocalDateTime start);

    List<BookingEntity> findAllByBooker_IdAndEndBeforeOrderByStart(Long booker_id, LocalDateTime start);

    List<BookingEntity> findAllByBooker_IdAndStatusEqualsOrderByStart(Long booker_id, BookingStatus status);

    List<BookingEntity> findAllByItem_Owner_IdOrderByStart(Long userId);

    List<BookingEntity> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStart
            (Long userId, LocalDateTime start, LocalDateTime end);

    List<BookingEntity> findAllByItem_Owner_IdAndStartAfterOrderByStart(Long userId, LocalDateTime start);

    List<BookingEntity> findAllByItem_Owner_IdAndEndBeforeOrderByStart(Long userId, LocalDateTime start);

    List<BookingEntity> findAllByItem_Owner_IdAndStatusEqualsOrderByStart(Long userId, BookingStatus status);
}
