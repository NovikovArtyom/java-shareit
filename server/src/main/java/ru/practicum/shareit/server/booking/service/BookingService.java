package ru.practicum.shareit.server.booking.service;


import ru.practicum.shareit.server.booking.model.BookingEntity;

import java.util.List;

public interface BookingService {
    BookingEntity addBooking(Long userId, BookingEntity bookingEntity, Long itemId);

    BookingEntity approveBooking(Long userId, Long bookingId, Boolean approved);

    BookingEntity getBookingById(Long userId, Long bookingId);

    List<BookingEntity> getBookingsByBooker(Long userId, String state, Integer from, Integer size);

    List<BookingEntity> getBookingByOwner(Long userId, String state, Integer from, Integer size);
}
