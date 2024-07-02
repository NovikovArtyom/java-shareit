package ru.practicum.shareit.server.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.UserEntity;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
public class ItemMapperTest {

    @Test
    public void toItemDtoSuccess() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Артём");
        user.setEmail("artyom@mail.ru");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(2L);

        ItemEntity item = new ItemEntity();
        item.setId(3L);
        item.setName("Дрель");
        item.setDescription("Дрель Мокито");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequest(itemRequest);

        ItemDto itemDto = ItemMapper.toItemDto(item);

        assertEquals(3L, itemDto.getId());
        assertEquals("Дрель", itemDto.getName());
        assertEquals("Дрель Мокито", itemDto.getDescription());
        assertTrue(itemDto.getAvailable());
        assertEquals(1L, itemDto.getOwner().getId());
        assertEquals(2L, itemDto.getRequestId());
    }

    @Test
    public void fromUpdatedItemDtoToItemEntitySuccess() {
        UpdatedItemDto updatedItemDto = new UpdatedItemDto();
        updatedItemDto.setId(3L);
        updatedItemDto.setName("Новая Дрель");
        updatedItemDto.setDescription("Новая Дрель Мокито");
        updatedItemDto.setAvailable(false);

        ItemEntity item = ItemMapper.fromUpdatedItemDtoToItemEntity(updatedItemDto);

        assertEquals(3L, item.getId());
        assertEquals("Новая Дрель", item.getName());
        assertEquals("Новая Дрель Мокито", item.getDescription());
        assertFalse(item.getAvailable());
        assertNull(item.getOwner());
        assertNull(item.getRequest());
    }

    @Test
    public void fromItemDtoToItemEntitySuccess() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        ItemDto itemDto = new ItemDto(3L, "Дрель", "Дрель Мокито", true,
                userDto, null,null,null,null);

        ItemEntity item = ItemMapper.fromItemDtoToItemEntity(itemDto);

        assertEquals(3L, item.getId());
        assertEquals("Дрель", item.getName());
        assertEquals("Дрель Мокито", item.getDescription());
        assertTrue(item.getAvailable());
        assertNotNull(item.getOwner());
        assertEquals(1L, item.getOwner().getId());
        assertNull(item.getRequest());
    }

    @Test
    public void toItemResponseDtoSuccess() {
        ItemEntity item = new ItemEntity();
        item.setId(3L);
        item.setName("Дрель");

        ItemResponseDto itemResponseDto = ItemMapper.toItemResponseDto(item);

        assertEquals(3L, itemResponseDto.getId());
        assertEquals("Дрель", itemResponseDto.getName());
    }

    @Test
    public void toItemDtoForItemRequestSuccess() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(2L);

        ItemEntity item = new ItemEntity();
        item.setId(3L);
        item.setName("Дрель");
        item.setDescription("Дрель Мокито");
        item.setAvailable(true);
        item.setRequest(itemRequest);

        ItemDtoForItemRequest itemDtoForItemRequest = ItemMapper.toItemDtoForItemRequest(item);

        assertEquals(3L, itemDtoForItemRequest.getId());
        assertEquals("Дрель", itemDtoForItemRequest.getName());
        assertEquals("Дрель Мокито", itemDtoForItemRequest.getDescription());
        assertTrue(itemDtoForItemRequest.getAvailable());
        assertEquals(2L, itemDtoForItemRequest.getRequestId());
    }
}
