package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private Long id;
    @NonNull
    @NotBlank
    private String description;
    @NonNull
    private User requestor;
    @NonNull
    private LocalDateTime created;
}
