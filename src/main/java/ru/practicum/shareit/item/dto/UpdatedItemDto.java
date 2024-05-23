package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UpdatedItemDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
}
