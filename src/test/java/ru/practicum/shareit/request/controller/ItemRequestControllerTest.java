package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.UserEntity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private UserEntity firstUserEntity = new UserEntity(1L, "Артём", "artyom@gmail.com");
    private UserEntity secondUserEntity = new UserEntity(2L, "Владимир", "vladimir@mail.ru");

    private ItemRequest firstItemRequest = new ItemRequest(
            1L,
            "Купить дрель",
            firstUserEntity,
            LocalDateTime.of(2024, 12, 10, 22, 2),
            Collections.emptyList()
    );
    private ItemRequest secondItemRequest = new ItemRequest(
            2L,
            "Купить дом",
            secondUserEntity,
            LocalDateTime.of(2024, 12, 10, 22, 2),
            Collections.emptyList()
    );

    private ItemRequestDtoRequest itemRequestDtoRequest = new ItemRequestDtoRequest("Купить дрель");

    @Test
    void testCreateItemRequest() throws Exception {
        when(itemRequestService.createItemRequest(any(Long.class), any(ItemRequest.class)))
                .thenReturn(firstItemRequest);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstItemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(firstItemRequest.getDescription()), String.class))
                .andExpect(jsonPath("$.requester.id", is(firstItemRequest.getRequester().getId()), Long.class))
                .andExpect(jsonPath("$.created",
                        is(firstItemRequest.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.items", is(firstItemRequest.getItems())));
    }

    @Test
    void testGetItemRequestByUserId() throws Exception {
        when(itemRequestService.getItemRequestByUserId(any(Long.class)))
                .thenReturn(List.of(firstItemRequest));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(firstItemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(firstItemRequest.getDescription()), String.class))
                .andExpect(jsonPath("$[0].requester.id", is(firstItemRequest.getRequester().getId()), Long.class))
                .andExpect(jsonPath("$[0].created",
                        is(firstItemRequest.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].items", is(firstItemRequest.getItems())));
    }

    @Test
    void testGetAllItemRequest() throws Exception {
        when(itemRequestService.getAllItemRequest(1L, 0, 10))
                .thenReturn(List.of(secondItemRequest));

        mvc.perform(get("/requests/all?from=0&size=10")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(secondItemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(secondItemRequest.getDescription()), String.class))
                .andExpect(jsonPath("$[0].requester.id", is(secondItemRequest.getRequester().getId()), Long.class))
                .andExpect(jsonPath("$[0].created",
                        is(secondItemRequest.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].items", is(secondItemRequest.getItems())));
    }

    @Test
    void testGetItemRequestByRequestId() throws Exception {
        when(itemRequestService.getItemRequestByRequestId(1L, 2L))
                .thenReturn(secondItemRequest);

        mvc.perform(get("/requests/2")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(secondItemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(secondItemRequest.getDescription()), String.class))
                .andExpect(jsonPath("$.requester.id", is(secondItemRequest.getRequester().getId()), Long.class))
                .andExpect(jsonPath("$.created",
                        is(secondItemRequest.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.items", is(secondItemRequest.getItems())));
    }
}
