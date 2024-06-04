package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.dto.UserMapper;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(ItemEntity item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                UserMapper.toUserDto(item.getOwner()),
                item.getRequest() != null ? ItemRequestMapper.toItemRequestDto(item.getRequest()) : null
        );
    }

    public static ItemEntity fromUpdatedItemDtoToItemEntity(UpdatedItemDto updatedItemDto) {
        return new ItemEntity(
                updatedItemDto.getId(),
                updatedItemDto.getName(),
                updatedItemDto.getDescription(),
                updatedItemDto.getAvailable(),
                null,
                null
        );
    }

    public static ItemEntity fromItemDtoToItemEntity(ItemDto itemDto) {
        return new ItemEntity(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null,
                null
        );
    }
}
