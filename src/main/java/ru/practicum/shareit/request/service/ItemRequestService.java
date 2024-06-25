package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest createItemRequest(Long userId, ItemRequest itemRequestEntity);

    List<ItemRequest> getItemRequestByUserId(Long userId);

    List<ItemRequest> getAllItemRequest(Long userId, Integer from, Integer size);

    ItemRequest getItemRequestByRequestId(Long userId, Long requestId);
}
