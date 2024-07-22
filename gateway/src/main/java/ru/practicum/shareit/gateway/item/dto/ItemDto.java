package ru.practicum.shareit.gateway.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ItemDto {
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    private String description;
    @NonNull
    private Boolean available;
    private Long requestId;
}
