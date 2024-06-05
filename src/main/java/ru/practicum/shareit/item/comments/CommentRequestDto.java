package ru.practicum.shareit.item.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.item.model.ItemEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentRequestDto {
    @NonNull
    private String text;
}
