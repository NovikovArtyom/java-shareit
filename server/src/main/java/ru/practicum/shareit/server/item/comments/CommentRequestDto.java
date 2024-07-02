package ru.practicum.shareit.server.item.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    @NonNull
    private String text;
}
