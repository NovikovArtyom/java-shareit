package ru.practicum.shareit.server.item.comments.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.server.booking.model.BookingStatus;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.exception.IncorrectCommentException;
import ru.practicum.shareit.server.exception.ItemNotFoundException;
import ru.practicum.shareit.server.item.comments.CommentEntity;
import ru.practicum.shareit.server.item.comments.CommentRepository;
import ru.practicum.shareit.server.item.model.ItemEntity;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.item.service.ItemServiceImpl;
import ru.practicum.shareit.server.user.model.UserEntity;
import ru.practicum.shareit.server.user.service.UserService;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
@ActiveProfiles("test")
public class CommentServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Long userId;
    private Long itemId;
    private UserEntity user;
    private ItemEntity item;
    private CommentEntity comment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = 1L;
        itemId = 1L;
        user = new UserEntity(userId, "Test User", "test@example.com");
        item = new ItemEntity(itemId, "Item", "Description", true, user, null);
        comment = new CommentEntity(1L, "Comment", item, user, LocalDateTime.now());
        ReflectionTestUtils.setField(itemService, "userService", userService);
        ReflectionTestUtils.setField(itemService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemService, "bookingRepository", bookingRepository);
        ReflectionTestUtils.setField(itemService, "commentRepository", commentRepository);
    }

    @Test
    public void addCommentSuccess() {
        when(userService.getUserById(userId)).thenReturn(user);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.existsByItem_IdAndBooker_IdAndStatusEqualsAndStartBefore(
                eq(itemId), eq(userId), eq(BookingStatus.APPROVED), any(LocalDateTime.class)))
                .thenReturn(true);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(comment);

        CommentEntity savedComment = itemService.addComment(userId, itemId, comment);

        assertNotNull(savedComment);
        assertEquals(comment.getText(), savedComment.getText());
        assertEquals(comment.getItem(), savedComment.getItem());
        assertEquals(comment.getAuthor(), savedComment.getAuthor());
        assertNotNull(savedComment.getCreated());
    }

    @Test
    public void addCommentItemNotFound() {
        when(userService.getUserById(userId)).thenReturn(user);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ItemNotFoundException.class, () -> {
            itemService.addComment(userId, itemId, comment);
        });

        String expectedMessage = "Данная вещь не найдена!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void addCommentIncorrectComment() {
        when(userService.getUserById(userId)).thenReturn(user);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.existsByItem_IdAndBooker_IdAndStatusEqualsAndStartBefore(
                eq(itemId), eq(userId), eq(BookingStatus.APPROVED), any(LocalDateTime.class)))
                .thenReturn(false);

        Exception exception = assertThrows(IncorrectCommentException.class, () -> {
            itemService.addComment(userId, itemId, comment);
        });

        String expectedMessage = "Оставить комментарий невозможно!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
