package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    List<BookingEntity> findAllByBooker_IdOrderByStartDesc(Long bookerId);

    List<BookingEntity> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime start, LocalDateTime end);

    List<BookingEntity> findAllByBooker_IdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<BookingEntity> findAllByBooker_IdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime start);

    List<BookingEntity> findAllByBooker_IdAndStatusEqualsOrderByStartDesc(Long bookerId, BookingStatus status);

    List<BookingEntity> findAllByItem_Owner_IdOrderByStartDesc(Long userId);

    List<BookingEntity> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId, LocalDateTime start, LocalDateTime end);

    List<BookingEntity> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime start);

    List<BookingEntity> findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime start);

    List<BookingEntity> findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(Long userId, BookingStatus status);

    BookingEntity findTop1ByItem_IdAndStartBeforeAndStatusEqualsOrderByStartDesc(Long itemId, LocalDateTime now, BookingStatus status);

    BookingEntity findTop1ByItem_IdAndStartAfterAndStatusEqualsOrderByStart(Long itemId, LocalDateTime now, BookingStatus status);

    Boolean existsByItem_IdAndBooker_IdAndStatusEqualsAndStartBefore(Long itemId, Long bookerId, BookingStatus status, LocalDateTime now);
}
