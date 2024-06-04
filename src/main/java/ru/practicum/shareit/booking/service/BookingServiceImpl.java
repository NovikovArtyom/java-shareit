package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.IncorrectDateException;
import ru.practicum.shareit.exception.IncorrectStateException;
import ru.practicum.shareit.exception.UserAccessException;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    @Transactional(readOnly = true)
    public BookingEntity getBookingById(Long userId, Long bookingId) {
        return bookingRepository.findByIdAndBooker_IdOrItem_Owner_Id(bookingId, userId, userId).orElseThrow(() ->
                new BookingNotFoundException("По данной выборке бронирование не найдено!"));
    }

    @Override
    public List<BookingEntity> getBookingsByBooker(Long userId, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBooker_IdOrderByStart(userId);
            case "CURRENT":
                return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStart(userId, now, now);
            case "PAST":
                return bookingRepository.findAllByBooker_IdAndStartAfterOrderByStart(userId, now);
            case "FUTURE":
                return bookingRepository.findAllByBooker_IdAndEndBeforeOrderByStart(userId, now);
            case "WAITING":
                return bookingRepository.findAllByBooker_IdAndStatusEqualsOrderByStart(userId, BookingStatus.WAITING);
            case "REJECTED":
                return bookingRepository.findAllByBooker_IdAndStatusEqualsOrderByStart(userId, BookingStatus.REJECTED);
            default:
                throw new IncorrectStateException("Указаный параметр state некорректен!");
        }
    }

    @Override
    public List<BookingEntity> getBookingByOwner(Long userId, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByItem_Owner_IdOrderByStart(userId);
            case "CURRENT":
                return bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStart(userId, now, now);
            case "PAST":
                return bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStart(userId, now);
            case "FUTURE":
                return bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByStart(userId, now);
            case "WAITING":
                return bookingRepository.findAllByItem_Owner_IdAndStatusEqualsOrderByStart(userId, BookingStatus.WAITING);
            case "REJECTED":
                return bookingRepository.findAllByItem_Owner_IdAndStatusEqualsOrderByStart(userId, BookingStatus.REJECTED);
            default:
                throw new IncorrectStateException("Указаный параметр state некорректен!");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingEntity addBooking(Long userId, BookingEntity bookingEntity, Long itemId) {
        UserEntity booker = userService.getUserById(userId);
        ItemEntity item = itemService.getItemById(itemId);
        if (bookingEntity.getStart().isBefore(bookingEntity.getEnd()) && (item.getAvailable())) {
            item.setAvailable(false);
            bookingEntity.setBooker(booker);
            bookingEntity.setItem(item);
            bookingEntity.setStatus(BookingStatus.WAITING);
            return bookingRepository.save(bookingEntity);
        } else {
            throw new IncorrectDateException("Дата старта аренды не может быть позднее даты конца аренды!");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingEntity approveBooking(Long userId, Long bookingId, Boolean approved) {
        UserEntity user = userService.getUserById(userId);
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Заявка о бронировании по дайнному Id не найдена!"));
        UserEntity owner = bookingEntity.getItem().getOwner();
        if (owner == user) {
            if (approved) {
                bookingEntity.setStatus(BookingStatus.APPROVED);
            } else {
                bookingEntity.setStatus(BookingStatus.REJECTED);
            }
            return bookingRepository.save(bookingEntity);
        } else {
            throw new UserAccessException("Изменять статус заявки на бронирование может только владелец вещи!");
        }
    }


}
