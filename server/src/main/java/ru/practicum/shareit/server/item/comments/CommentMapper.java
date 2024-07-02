package ru.practicum.shareit.server.item.comments;
import ru.practicum.shareit.server.item.comments.CommentResponseDto;
import ru.practicum.shareit.server.item.comments.CommentRequestDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMapper {
    public static CommentResponseDto toDto(CommentEntity commentEntity) {
        return new CommentResponseDto(
                commentEntity.getId(),
                commentEntity.getText(),
                commentEntity.getAuthor().getName(),
                commentEntity.getCreated()
        );
    }

    public static CommentEntity toEntity(CommentRequestDto commentRequestDto) {
        return new CommentEntity(
                null,
                commentRequestDto.getText(),
                null,
                null,
                null
        );
    }
}
