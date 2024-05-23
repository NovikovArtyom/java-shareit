package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository {
    Item addItem(Item itemEntity);

    Item getItemById(Long itemId);

    Item updateItem(Long itemId, UpdatedItemDto updatedItemDto);
}
