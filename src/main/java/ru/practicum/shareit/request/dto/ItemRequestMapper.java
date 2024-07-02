package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestDtoResponse toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDtoResponse(
                itemRequest.getId(),
                itemRequest.getDescription(),
                UserMapper.toUserResponseDto(itemRequest.getRequester()),
                itemRequest.getCreated(),
                itemRequest.getItems() != null ? itemRequest.getItems().stream()
                        .map(ItemMapper::toItemDtoForItemRequest)
                        .collect(Collectors.toList()) : null
        );
    }

    public static ItemRequest toItemRequestEntity(ItemRequestDtoRequest itemRequestDtoRequest) {
        return new ItemRequest(
                null,
                itemRequestDtoRequest.getDescription(),
                null,
                null,
                null
        );
    }
}
