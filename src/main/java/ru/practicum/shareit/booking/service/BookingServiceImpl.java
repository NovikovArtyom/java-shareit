package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.LastAndNextBookingDto;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private LocalDateTime now = LocalDateTime.now();

    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    @Transactional(readOnly = true)
    public BookingEntity getBookingById(Long userId, Long bookingId) {
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Бронь по заданному ID не найдена"));
        if (bookingEntity.getBooker().getId() == userId || bookingEntity.getItem().getOwner().getId() == userId) {
            return bookingEntity;
        } else {
            throw new UserAccessException("Просматривать данные о бронировании может только владелец вещи или автор брони!");
        }
    }

    @Override
    public List<BookingEntity> getBookingsByBooker(Long userId, String state) {
        if (userService.getUserById(userId) != null) {
            LocalDateTime now = LocalDateTime.now();
            switch (state) {
                case "ALL":
                    return bookingRepository.findAllByBooker_IdOrderByStartDesc(userId);
                case "CURRENT":
                    return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
                case "PAST":
                    return bookingRepository.findAllByBooker_IdAndEndBeforeOrderByStartDesc(userId, now);
                case "FUTURE":
                    return bookingRepository.findAllByBooker_IdAndStartAfterOrderByStartDesc(userId, now);
                case "WAITING":
                    return bookingRepository.findAllByBooker_IdAndStatusEqualsOrderByStartDesc(userId, BookingStatus.WAITING);
                case "REJECTED":
                    return bookingRepository.findAllByBooker_IdAndStatusEqualsOrderByStartDesc(userId, BookingStatus.REJECTED);
                default:
                    throw new IncorrectStateException("Указаный параметр state некорректен!");
            }
        } else {
            throw new UserNotFoundException("Пользователь с данным ID не найден!");
        }
    }

    @Override
    public List<BookingEntity> getBookingByOwner(Long userId, String state) {
        if (userService.getUserById(userId) != null) {
            LocalDateTime now = LocalDateTime.now();
            switch (state) {
                case "ALL":
                    return bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId);
                case "CURRENT":
                    return bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
                case "PAST":
                    return bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, now);
                case "FUTURE":
                    return bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, now);
                case "WAITING":
                    return bookingRepository.findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(userId, BookingStatus.WAITING);
                case "REJECTED":
                    return bookingRepository.findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(userId, BookingStatus.REJECTED);
                default:
                    throw new IncorrectStateException("Указаный параметр state некорректен!");
            }
        } else {
            throw new UserNotFoundException("Пользователь с данным ID не найден!");
        }
    }

    @Override
    public LastAndNextBookingDto getLastBookings(Long itemId) {
        return BookingMapper.toLastAndNextBookingDto
                (bookingRepository.findTop1ByItem_IdAndStartBeforeOrderByStartDesc(itemId, now));
    }

    @Override
    public LastAndNextBookingDto getNextBookings(Long itemId) {
        return BookingMapper.toLastAndNextBookingDto
                (bookingRepository.findTop1ByItem_IdAndStartAfterOrderByStart(itemId, now));
    }




    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingEntity addBooking(Long userId, BookingEntity bookingEntity, Long itemId) {
        UserEntity booker = userService.getUserById(userId);
        ItemEntity item = itemService.getItemById(itemId);
        if (bookingEntity.getStart().isBefore(bookingEntity.getEnd()) &&
                (!bookingEntity.getStart().isBefore(LocalDateTime.now()))) {
            if (item.getAvailable()) {
                //item.setAvailable(false);
                bookingEntity.setBooker(booker);
                bookingEntity.setItem(item);
                bookingEntity.setStatus(BookingStatus.WAITING);
                return bookingRepository.save(bookingEntity);
            } else {
                throw new ItemNotAvailableException("Данный товар уже забронирован!");
            }
        } else {
            throw new IncorrectDateException("Указаны некорректные даты бронирования!");
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
