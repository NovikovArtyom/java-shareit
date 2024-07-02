package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.booking.dto.LastAndNextBookingDto;
import ru.practicum.shareit.item.comments.CommentResponseDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @NotBlank
    private String description;
    @NonNull
    private Boolean available;
    private UserDto owner;
    private Long requestId;
    private LastAndNextBookingDto lastBooking;
    private LastAndNextBookingDto nextBooking;
    private List<CommentResponseDto> comments;
}
