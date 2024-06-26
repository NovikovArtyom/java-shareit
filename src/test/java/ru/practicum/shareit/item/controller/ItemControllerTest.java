package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.LastAndNextBookingDto;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.item.comments.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    ItemService itemService;
    @MockBean
    UserService userService;

    private UserEntity user;
    private UserDto userDto;
    private ItemEntity item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        user = new UserEntity(1L, "Артём", "artyom@gmail.com");
        userDto = new UserDto(1L, "Артём", "artyom@gmail.com");
        item = new ItemEntity(1L, "Дрель", "Дрель Мокито", true, user, null);
        itemDto = new ItemDto(1L, "Дрель", "Дрель Мокито", true, userDto, null, null, null, null);

        when(userService.getUserById(user.getId())).thenReturn(user);
        when(itemService.getAllItems(user.getId(), 0, 10)).thenReturn(List.of(itemDto));
    }

    @Test
    void testGetAllItems() throws Exception {
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].owner.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].owner.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$[0].owner.email", is(userDto.getEmail()), Boolean.class))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking", is(itemDto.getLastBooking()), LastAndNextBookingDto.class))
                .andExpect(jsonPath("$[0].nextBooking", is(itemDto.getNextBooking()), LastAndNextBookingDto.class))
                .andExpect(jsonPath("$[0].comments", is(itemDto.getComments()), CommentResponseDto.class));
    }

    @Test
    void testGetItemById() throws Exception {
        when(itemService.getItemById(user.getId(), item.getId())).thenReturn(itemDto);

        mvc.perform(get("/items/{itemId}", item.getId())
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.owner.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.owner.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.owner.email", is(userDto.getEmail()), String.class))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking()), LastAndNextBookingDto.class))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking()), LastAndNextBookingDto.class))
                .andExpect(jsonPath("$.comments", is(itemDto.getComments()), CommentResponseDto.class));
    }

    @Test
    void testSearchItems() throws Exception {
        when(itemService.search(0, 10, "дрель")).thenReturn(List.of(item));

        mvc.perform(get("/items/search?text=дрель&from=0&size=10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].owner.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].owner.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$[0].owner.email", is(userDto.getEmail()), String.class))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking", is(itemDto.getLastBooking()), LastAndNextBookingDto.class))
                .andExpect(jsonPath("$[0].nextBooking", is(itemDto.getNextBooking()), LastAndNextBookingDto.class))
                .andExpect(jsonPath("$[0].comments", is(itemDto.getComments()), CommentResponseDto.class));
    }

    @Test
    void testAddItem() throws Exception {
        when(itemService.addItem(user.getId(), null, item)).thenReturn(item);
        ItemDto newItemDto = new ItemDto(null, "Новая Дрель", "Новая Дрель Мокито", true, userDto, null, null, null, null);
        ItemDto returnedItemDto = new ItemDto(2L, "Новая Дрель", "Новая Дрель Мокито", true, userDto, null, null, null, null);

        when(itemService.addItem(user.getId(), null, ItemMapper.fromItemDtoToItemEntity(newItemDto)))
                .thenReturn(ItemMapper.fromItemDtoToItemEntity(returnedItemDto));

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(newItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(returnedItemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(returnedItemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(returnedItemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.owner.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.owner.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.owner.email", is(userDto.getEmail()), String.class))
                .andExpect(jsonPath("$.requestId", is(returnedItemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$.lastBooking", is(returnedItemDto.getLastBooking()), LastAndNextBookingDto.class))
                .andExpect(jsonPath("$.nextBooking", is(returnedItemDto.getNextBooking()), LastAndNextBookingDto.class))
                .andExpect(jsonPath("$.comments", is(returnedItemDto.getComments()), CommentResponseDto.class));
    }

    @Test
    void testUpdateItem() throws Exception {
        UpdatedItemDto updatedItemDto = new UpdatedItemDto(item.getId(), "Новая дрель Мокито" , "Обновленная Дрель Мокито", true);
        ItemDto updatedItemDtoResult = new ItemDto(item.getId(), "Обновленная Дрель", "Обновленная Дрель Мокито", true, userDto, null, null, null, null);

        when(itemService.updateItem(user.getId(), item.getId(), updatedItemDto))
                .thenReturn(ItemMapper.fromItemDtoToItemEntity(updatedItemDtoResult));

        mvc.perform(patch("/items/{itemId}", item.getId())
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(updatedItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedItemDtoResult.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updatedItemDtoResult.getName()), String.class))
                .andExpect(jsonPath("$.description", is(updatedItemDtoResult.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(updatedItemDtoResult.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.owner.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.owner.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.owner.email", is(userDto.getEmail()), String.class))
                .andExpect(jsonPath("$.requestId", is(updatedItemDtoResult.getRequestId()), Long.class))
                .andExpect(jsonPath("$.lastBooking", is(updatedItemDtoResult.getLastBooking()), LastAndNextBookingDto.class))
                .andExpect(jsonPath("$.nextBooking", is(updatedItemDtoResult.getNextBooking()), LastAndNextBookingDto.class))
                .andExpect(jsonPath("$.comments", is(updatedItemDtoResult.getComments()), CommentResponseDto.class));
    }
}
