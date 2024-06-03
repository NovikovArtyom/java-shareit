package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.ItemEntity;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(ItemEntity item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest()
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
                itemDto.getOwner(),
                itemDto.getRequest()
        );
    }
}
