package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        log.info(String.format("Получение записи о бронировании с id = %d для пользователя с id = %d", bookingId, userId));
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Бронь по заданному ID не найдена"));
        if (bookingEntity.getBooker().getId().equals(userId) || bookingEntity.getItem().getOwner().getId().equals(userId)) {
            return bookingEntity;
        } else {
            throw new UserAccessException("Просматривать данные о бронировании может только владелец вещи или автор брони!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingEntity> getBookingsByBooker(Long userId, String state, Integer from, Integer size) {
        log.info(String.format("Получение записи о бронировании типа %s для пользователя-арендатора с id = %d", state, userId));
        if (userService.getUserById(userId) != null) {
            Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
            LocalDateTime now = LocalDateTime.now();
            switch (state) {
                case "ALL":
                    return bookingRepository.findAllByBooker_Id(userId, pageable).stream().collect(Collectors.toList());
                case "CURRENT":
                    return bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(userId, now, now, pageable)
                            .stream().collect(Collectors.toList());
                case "PAST":
                    return bookingRepository.findAllByBooker_IdAndEndBefore(userId, now, pageable)
                            .stream().collect(Collectors.toList());
                case "FUTURE":
                    return bookingRepository.findAllByBooker_IdAndStartAfter(userId, now, pageable)
                            .stream().collect(Collectors.toList());
                case "WAITING":
                    return bookingRepository.findAllByBooker_IdAndStatusEquals(userId, BookingStatus.WAITING, pageable)
                            .stream().collect(Collectors.toList());
                case "REJECTED":
                    return bookingRepository.findAllByBooker_IdAndStatusEquals(userId, BookingStatus.REJECTED, pageable)
                            .stream().collect(Collectors.toList());
                default:
                    throw new IncorrectStatusException(state);
            }
        } else {
            throw new UserNotFoundException("Пользователь с данным ID не найден!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingEntity> getBookingByOwner(Long userId, String state, Integer from, Integer size) {
        log.info(String.format("Получение записи о бронировании типа %s для пользователя-владельца с id = %d", state, userId));
        if (userService.getUserById(userId) != null) {
            Pageable pageable = PageRequest.of(from, size);
            LocalDateTime now = LocalDateTime.now();
            switch (state) {
                case "ALL":
                    return bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId, pageable).stream().collect(Collectors.toList());
                case "CURRENT":
                    return bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now, pageable)
                            .stream().collect(Collectors.toList());
                case "PAST":
                    return bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, now, pageable)
                            .stream().collect(Collectors.toList());
                case "FUTURE":
                    return bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, now, pageable)
                            .stream().collect(Collectors.toList());
                case "WAITING":
                    return bookingRepository.findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(userId, BookingStatus.WAITING, pageable)
                            .stream().collect(Collectors.toList());
                case "REJECTED":
                    return bookingRepository.findAllByItem_Owner_IdAndStatusEqualsOrderByStartDesc(userId, BookingStatus.REJECTED, pageable)
                            .stream().collect(Collectors.toList());
                default:
                    throw new IncorrectStatusException(state);
            }
        } else {
            throw new UserNotFoundException("Пользователь с данным ID не найден!");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingEntity addBooking(Long userId, BookingEntity bookingEntity, Long itemId) {
        log.info(String.format("Добавление записи о бронировании вещи с id = %d пользователем с id = %d", itemId, userId));
        UserEntity booker = userService.getUserById(userId);
        ItemEntity item = ItemMapper.fromItemDtoToItemEntity(itemService.getItemById(userId, itemId));
        if (bookingEntity.getStart().isBefore(bookingEntity.getEnd()) &&
                (!bookingEntity.getStart().isBefore(LocalDateTime.now()))) {
            if (item.getAvailable()) {
                if (!booker.getId().equals(item.getOwner().getId())) {
                    bookingEntity.setBooker(booker);
                    bookingEntity.setItem(item);
                    bookingEntity.setStatus(BookingStatus.WAITING);
                    return bookingRepository.save(bookingEntity);
                } else {
                    throw new UserAccessException("Пользователь не может забронировать свой товар!");
                }
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
        log.info(String.format("Подтверждение записи о бронировании с id = %d пользователем с id = %d", bookingId, userId));
        UserEntity user = userService.getUserById(userId);
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Заявка о бронировании по дайнному Id не найдена!"));
        UserEntity owner = bookingEntity.getItem().getOwner();
        if (owner == user) {
            if (bookingEntity.getStatus() == BookingStatus.WAITING) {
                if (approved) {
                    bookingEntity.setStatus(BookingStatus.APPROVED);
                } else {
                    bookingEntity.setStatus(BookingStatus.REJECTED);
                }
                return bookingRepository.save(bookingEntity);
            } else {
                throw new RepeatedApproveException("Подтвердить бронирование можно только оно в статусе WAITING!");
            }
        } else {
            throw new UserAccessException("Изменять статус заявки на бронирование может только владелец вещи!");
        }
    }
}
