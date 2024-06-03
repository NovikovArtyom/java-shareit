package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(Long userID, ItemDto itemDto);

    ItemDto getItemById(Long itemId);

    ItemDto updateItem(Long userId, Long itemId, UpdatedItemDto updatedItemDto);

    Collection<ItemDto> getAllItems(Long userId);

    Collection<ItemDto> search(String text);
}
