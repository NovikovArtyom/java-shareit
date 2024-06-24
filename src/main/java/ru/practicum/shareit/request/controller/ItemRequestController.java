package ru.practicum.shareit.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String USER_ID = "X-Sharer-User-Id";

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDtoResponse>> getItemRequestByUserId(@RequestHeader(USER_ID) Long userId) {
        List<ItemRequestDtoResponse> itemRequest = itemRequestService.getItemRequestByUserId(userId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemRequest);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDtoResponse>> getAllItemRequest(@RequestHeader(USER_ID) Long userId,
                                                                          @RequestParam(defaultValue = "0") Integer from,
                                                                          @RequestParam(defaultValue = "10") Integer size) {
        List<ItemRequestDtoResponse> itemRequest = itemRequestService.getAllItemRequest(userId, from, size).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemRequest);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDtoResponse> getItemRequestByRequestId(@RequestHeader(USER_ID) Long userId,
                                                                            @PathVariable Long requestId) {
        return ResponseEntity.ok(ItemRequestMapper.toItemRequestDto(itemRequestService.getItemRequestByRequestId(userId, requestId)));
    }

    @PostMapping
    public ResponseEntity<ItemRequestDtoResponse> createItemRequest(@RequestHeader(USER_ID) Long userId,
                                                                    @Validated @RequestBody ItemRequestDtoRequest itemRequestDtoRequest) {
        return ResponseEntity.ok(ItemRequestMapper.toItemRequestDto(itemRequestService.createItemRequest(userId,
                ItemRequestMapper.toItemRequestEntity(itemRequestDtoRequest))));
    }
}
