package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comments.CommentEntity;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.ItemEntity;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAllItems(Long userId);

    ItemDto getItemById(Long itemId);

    List<ItemEntity> search(String text);

    ItemEntity addItem(Long userId, ItemEntity item);

    ItemEntity updateItem(Long userId, Long itemId, UpdatedItemDto updatedItemDto);

    CommentEntity addComment(Long userId, Long itemId, CommentEntity commentEntity);
}
