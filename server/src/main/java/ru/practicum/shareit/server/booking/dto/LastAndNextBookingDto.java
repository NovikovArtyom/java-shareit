package ru.practicum.shareit.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LastAndNextBookingDto {
    private Long id;
    private Long bookerId;
}
