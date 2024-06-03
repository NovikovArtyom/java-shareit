package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.ItemEntity;

import java.util.List;

public interface ItemService {
    List<ItemEntity> getAllItems(Long userId);

    ItemEntity getItemById(Long itemId);

    List<ItemEntity> search(String text);

    ItemEntity addItem(Long userId, ItemEntity item);

    ItemEntity updateItem(Long userId, Long itemId, UpdatedItemDto updatedItemDto);
}
