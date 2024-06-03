package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.user.model.UserEntity;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    @NonNull
    private LocalDateTime start;
    @NonNull
    private LocalDateTime end;
    @NonNull
    private ItemEntity item;
    @NonNull
    private UserEntity booker;
    @NonNull
    private BookingStatus status;
}
