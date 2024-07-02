package ru.practicum.shareit.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.server.booking.dto.LastAndNextBookingDto;
import ru.practicum.shareit.server.item.comments.CommentResponseDto;
import ru.practicum.shareit.server.user.dto.UserDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private Boolean available;
    private UserDto owner;
    private Long requestId;
    private LastAndNextBookingDto lastBooking;
    private LastAndNextBookingDto nextBooking;
    private List<CommentResponseDto> comments;
}
