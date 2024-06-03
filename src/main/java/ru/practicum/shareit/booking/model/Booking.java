package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
    private Item item;
    @NonNull
    private User booker;
    @NonNull
    private BookingStatus status;
}
