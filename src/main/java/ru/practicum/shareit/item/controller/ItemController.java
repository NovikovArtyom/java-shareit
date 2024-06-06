package ru.practicum.shareit.item.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.CommentMapper;
import ru.practicum.shareit.item.comments.CommentRequestDto;
import ru.practicum.shareit.item.comments.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID = "X-Sharer-User-Id";

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Collection<ItemDto> getAllItems(@RequestHeader(USER_ID) Long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(USER_ID) Long userId,@PathVariable Long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_ID) Long userId, @RequestBody @Validated ItemDto itemDto) {
        return ItemMapper.toItemDto(itemService.addItem(userId, ItemMapper.fromItemDtoToItemEntity(itemDto)));
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
