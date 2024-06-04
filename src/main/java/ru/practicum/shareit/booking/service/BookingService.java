package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingEntity;

import java.util.List;

public interface BookingService {
    BookingEntity addBooking(Long userId, BookingEntity bookingEntity, Long itemId);

    BookingEntity approveBooking(Long userId, Long bookingId, Boolean approved);

    BookingEntity getBookingById(Long userId, Long bookingId);

    List<BookingEntity> getBookingsByBooker(Long userId, String state);

    List<BookingEntity> getBookingByOwner(Long userId, String state);
}
