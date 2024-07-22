package ru.practicum.shareit.server.item.service;

import ru.practicum.shareit.server.item.comments.CommentEntity;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.UpdatedItemDto;
import ru.practicum.shareit.server.item.model.ItemEntity;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAllItems(Long userId, Integer form, Integer size);

    ItemDto getItemById(Long userId, Long itemId);

    List<ItemEntity> search(Integer from, Integer size, String text);

    ItemEntity addItem(Long userId, Long requestId, ItemEntity item);

    ItemEntity updateItem(Long userId, Long itemId, UpdatedItemDto updatedItemDto);

    CommentEntity addComment(Long userId, Long itemId, CommentEntity commentEntity);
}
