package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class BookingDto {
    private Long id;
    @NonNull
    private LocalDateTime start;
    @NonNull
    private LocalDateTime end;
    //private ItemEntity item;
    @NonNull
    private Long itemId;
    //private UserEntity booker;
    private Long bookerId;
    private BookingStatus status;
}
