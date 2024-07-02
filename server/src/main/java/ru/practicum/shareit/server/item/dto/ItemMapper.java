package ru.practicum.shareit.server.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.server.item.model.ItemEntity;
import ru.practicum.shareit.server.user.dto.UserMapper;
import ru.practicum.shareit.server.item.dto.UpdatedItemDto;
import ru.practicum.shareit.server.item.dto.ItemResponseDto;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(ItemEntity item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                UserMapper.toUserDto(item.getOwner()),
                item.getRequest() != null ? item.getRequest().getId() : null,
                null,
                null,
                null
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
                itemDto.getOwner() != null ? UserMapper.fromUserDtoToUserEntity(itemDto.getOwner()) : null,
                null
        );
    }

    public static ItemResponseDto toItemResponseDto(ItemEntity item) {
        return new ItemResponseDto(
                item.getId(),
                item.getName()
        );
    }

    public static ItemDtoForItemRequest toItemDtoForItemRequest(ItemEntity item) {
        return new ItemDtoForItemRequest(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest().getId()
        );
    }
}
