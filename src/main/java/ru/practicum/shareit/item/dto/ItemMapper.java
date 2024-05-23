package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest()
        );
    }

    public static Item fromUpdatedItemDtoToItemEntity(UpdatedItemDto updatedItemDto) {
        return new Item(
                updatedItemDto.getId(),
                updatedItemDto.getName(),
                updatedItemDto.getDescription(),
                updatedItemDto.getAvailable(),
                null,
                null
        );
    }

    public static Item fromItemDtoToItemEntity(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner(),
                itemDto.getRequest()
        );
    }
}
