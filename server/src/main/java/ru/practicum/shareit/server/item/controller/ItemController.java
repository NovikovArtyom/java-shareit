package ru.practicum.shareit.server.item.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.server.item.comments.CommentMapper;
import ru.practicum.shareit.server.item.comments.CommentRequestDto;
import ru.practicum.shareit.server.item.comments.CommentResponseDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.ItemMapper;
import ru.practicum.shareit.server.item.dto.UpdatedItemDto;
import ru.practicum.shareit.server.item.service.ItemService;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID = "X-Sharer-User-Id";

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Collection<ItemDto> getAllItems(@RequestHeader(USER_ID) Long userId,
                                           @RequestParam(defaultValue = "0") Integer form,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return itemService.getAllItems(userId, form, size);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "10") Integer size,
                                @RequestParam String text) {
        return itemService.search(from, size, text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_ID) Long userId, @Validated @RequestBody ItemDto itemDto) {
        Long itemRequestId = itemDto.getRequestId();
        return ItemMapper.toItemDto(itemService.addItem(userId, itemRequestId, ItemMapper.fromItemDtoToItemEntity(itemDto)));
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader(USER_ID) Long userId,
                                         @PathVariable Long itemId,
                                         @Validated @RequestBody CommentRequestDto commentRequestDto) {
        return CommentMapper.toDto(itemService.addComment(userId, itemId, CommentMapper.toEntity(commentRequestDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID) Long userId,
                              @PathVariable Long itemId, @RequestBody UpdatedItemDto updatedItemDto) {
        return ItemMapper.toItemDto(itemService.updateItem(userId, itemId, updatedItemDto));
    }
}
