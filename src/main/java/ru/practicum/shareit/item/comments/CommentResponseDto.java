package ru.practicum.shareit.item.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.UserEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
