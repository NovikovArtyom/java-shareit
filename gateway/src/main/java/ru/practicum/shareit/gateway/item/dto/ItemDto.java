package ru.practicum.shareit.gateway.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ItemDto {
    @NonNull
    @NotBlank
    @JsonProperty("name")
    private String name;
    @NonNull
    @JsonProperty("description")
    private String description;
    @NonNull
    @JsonProperty("available")
    private Boolean available;
    @JsonProperty("requestId")
    private Long requestId;
}
