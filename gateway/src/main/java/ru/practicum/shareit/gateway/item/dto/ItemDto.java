package ru.practicum.shareit.gateway.item.dto;

import lombok.NonNull;

import java.util.List;

public class ItemDto {
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private Boolean available;
    private Long requestId;
}
