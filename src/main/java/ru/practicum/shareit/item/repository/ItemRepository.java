package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item addItem(Item itemEntity);

    Item getItemById(Long itemId);

    Item updateItem(Long itemId, Item item);

    Collection<Item> getAllItems(Long userId);

    Collection<Item> search(String text);
}
