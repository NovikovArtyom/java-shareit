package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserAccessException;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemServiceImpl itemService;

    private UserEntity user;
    private ItemEntity item;
    private UpdatedItemDto updatedItemDto;

    @BeforeEach
    public void setUp() {
        user = new UserEntity();
        user.setId(1L);

        item = new ItemEntity();
        item.setId(1L);
        item.setName("Дрель");
        item.setDescription("Дрель Мокито");
        item.setAvailable(true);
        item.setOwner(user);

        updatedItemDto = new UpdatedItemDto();
        updatedItemDto.setName("Новая Дрель");
        updatedItemDto.setDescription("Новая Дрель Мокито");
        updatedItemDto.setAvailable(false);
    }

    @Test
    public void testUpdateItem_Success() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(item);

        ItemEntity updatedItem = itemService.updateItem(1L, 1L, updatedItemDto);

        assertEquals("Новая Дрель", updatedItem.getName());
        assertEquals("Новая Дрель Мокито", updatedItem.getDescription());
        assertFalse(updatedItem.getAvailable());
        verify(itemRepository, times(1)).save(any(ItemEntity.class));
    }

    @Test
    public void testUpdateItem_ItemNotFound() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            itemService.updateItem(1L, 1L, updatedItemDto);
        });

        assertEquals("Вещь c id = 1 не зарегистрирован!", exception.getMessage());
        verify(itemRepository, never()).save(any(ItemEntity.class));
    }

    @Test
    public void testUpdateItem_UserNotOwner() {
        UserEntity anotherUser = new UserEntity();
        anotherUser.setId(2L);
        item.setOwner(anotherUser);

        when(userService.getUserById(anyLong())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        UserAccessException exception = assertThrows(UserAccessException.class, () -> {
            itemService.updateItem(1L, 1L, updatedItemDto);
        });

        assertEquals("Редактировать информацию о вещи может только ее владелец!", exception.getMessage());
        verify(itemRepository, never()).save(any(ItemEntity.class));
    }

    @Test
    public void testUpdateItem_PartialUpdate() {
        updatedItemDto.setName(null);
        updatedItemDto.setAvailable(null);

        when(userService.getUserById(anyLong())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(item);

        ItemEntity updatedItem = itemService.updateItem(1L, 1L, updatedItemDto);

        assertEquals("Дрель", updatedItem.getName());
        assertEquals("Новая Дрель Мокито", updatedItem.getDescription());
        assertTrue(updatedItem.getAvailable());
        verify(itemRepository, times(1)).save(any(ItemEntity.class));
    }
}
