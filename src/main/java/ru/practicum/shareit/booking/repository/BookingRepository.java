package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    Page<BookingEntity> findAllByBooker_Id(Long bookerId, Pageable pageable);

    Page<BookingEntity> findAllByBooker_IdAndStartBeforeAndEndAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<BookingEntity> findAllByBooker_IdAndStartAfter(Long bookerId, LocalDateTime start, Pageable pageable);

    Page<BookingEntity> findAllByBooker_IdAndEndBefore(Long bookerId, LocalDateTime start, Pageable pageable);

    Page<BookingEntity> findAllByBooker_IdAndStatusEquals(Long bookerId, BookingStatus status, Pageable pageable);

    Page<BookingEntity> findAllByItem_Owner_IdOrderByStartDesc(Long userId, Pageable pageable);

    Page<BookingEntity> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<BookingEntity> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime start, Pageable pageable);

    Page<BookingEntity> findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime start, Pageable pageable);

    Page<BookingEntity> findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(Long userId, BookingStatus status, Pageable pageable);

    BookingEntity findTop1ByItem_IdAndStartBeforeAndStatusEqualsOrderByStartDesc(Long itemId, LocalDateTime now, BookingStatus status);

    BookingEntity findTop1ByItem_IdAndStartAfterAndStatusEqualsOrderByStart(Long itemId, LocalDateTime now, BookingStatus status);

    Boolean existsByItem_IdAndBooker_IdAndStatusEqualsAndStartBefore(Long itemId, Long bookerId, BookingStatus status, LocalDateTime now);
}
