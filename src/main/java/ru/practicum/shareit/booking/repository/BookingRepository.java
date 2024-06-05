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

    List<BookingEntity> findAllByBooker_IdOrderByStartDesc(Long bookerId);

    List<BookingEntity> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long booker_id, LocalDateTime start, LocalDateTime end);

    List<BookingEntity> findAllByBooker_IdAndStartAfterOrderByStartDesc(Long booker_id, LocalDateTime start);

    List<BookingEntity> findAllByBooker_IdAndEndBeforeOrderByStartDesc(Long booker_id, LocalDateTime start);

    List<BookingEntity> findAllByBooker_IdAndStatusEqualsOrderByStartDesc(Long booker_id, BookingStatus status);

    List<BookingEntity> findAllByItem_Owner_IdOrderByStartDesc(Long userId);

    List<BookingEntity> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc
            (Long userId, LocalDateTime start, LocalDateTime end);

    List<BookingEntity> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime start);

    List<BookingEntity> findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime start);

    List<BookingEntity> findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(Long userId, BookingStatus status);

    BookingEntity findTop1ByItem_IdAndStartBeforeOrderByStartDesc(Long itemId, LocalDateTime now);

    BookingEntity findTop1ByItem_IdAndStartAfterOrderByStart(Long itemId, LocalDateTime now);

    BookingEntity findByItem_IdAndBooker_Id(Long itemId, Long bookerId);
}
