package ru.practicum.shareit.gateway.item.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentController {
    private final CommentClient commentClient;

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Positive @PathVariable Long itemId,
                                             @RequestBody @Valid CommentRequestDto commentRequestDto) {
        log.info("Add new comment with userId={}, itemId={}", userId, itemId);
        return commentClient.addComment(userId, itemId, commentRequestDto);
    }


}
