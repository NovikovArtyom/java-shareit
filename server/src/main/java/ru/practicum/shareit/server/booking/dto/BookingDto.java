package ru.practicum.shareit.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.server.booking.model.BookingStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingDto {
    private Long id;
    @NonNull
    private LocalDateTime start;
    @NonNull
    private LocalDateTime end;
    @NonNull
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;
}
