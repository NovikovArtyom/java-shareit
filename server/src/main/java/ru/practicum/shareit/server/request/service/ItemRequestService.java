package ru.practicum.shareit.server.request.service;

import ru.practicum.shareit.server.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest createItemRequest(Long userId, ItemRequest itemRequestEntity);

    List<ItemRequest> getItemRequestByUserId(Long userId);

    List<ItemRequest> getAllItemRequest(Long userId, Integer from, Integer size);

    ItemRequest getItemRequestByRequestId(Long userId, Long requestId);
}
