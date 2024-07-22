package ru.practicum.shareit.server.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.server.item.dto.ItemDtoForItemRequest;
import ru.practicum.shareit.server.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDtoResponse {
    private Long id;
    private String description;
    private UserResponseDto requester;
    private LocalDateTime created;
    private List<ItemDtoForItemRequest> items;
}
