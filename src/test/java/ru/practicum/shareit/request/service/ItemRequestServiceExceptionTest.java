package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
@ActiveProfiles("test")
public class ItemRequestServiceExceptionTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetItemRequestByUserId_UserNotFound() {
        when(userService.getUserById(anyLong())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> itemRequestService.getItemRequestByUserId(1L));
    }

    @Test
    void getAllItemRequestUserNotFound() {
        when(userService.getUserById(anyLong())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> itemRequestService.getAllItemRequest(1L, 0, 10));
    }

    @Test
    void getItemRequestByRequestIdUserNotFound() {
        when(userService.getUserById(anyLong())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> itemRequestService.getItemRequestByRequestId(1L, 1L));
    }

    @Test
    void getItemRequestByRequestIdItemRequestNotFound() {
        UserEntity user = new UserEntity();
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ItemRequestNotFoundException.class, () -> itemRequestService.getItemRequestByRequestId(1L, 1L));
    }

    @Test
    void createItemRequestUserNotFound() {
        when(userService.getUserById(anyLong())).thenReturn(null);

        ItemRequest itemRequest = new ItemRequest();
        assertThrows(UserNotFoundException.class, () -> itemRequestService.createItemRequest(1L, itemRequest));
    }
}
