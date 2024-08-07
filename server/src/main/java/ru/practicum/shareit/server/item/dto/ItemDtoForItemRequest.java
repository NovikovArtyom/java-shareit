package ru.practicum.shareit.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoForItemRequest {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
