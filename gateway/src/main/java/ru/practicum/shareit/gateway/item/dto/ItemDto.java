package ru.practicum.shareit.gateway.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;

public class ItemDto {
    @NonNull
    @JsonProperty("name")
    private String name;
    @NonNull
    @JsonProperty("description")
    private String description;
    @NonNull
    @JsonProperty("available")
    private Boolean available;
    private Long requestId;
}
