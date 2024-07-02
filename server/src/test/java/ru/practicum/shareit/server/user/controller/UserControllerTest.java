package ru.practicum.shareit.server.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    private final UserEntity firstUser = new UserEntity(1L, "Артём", "artyom@gmail.com");
    private final UserEntity secondUser = new UserEntity(2L, "Владимир", "vladimir@mail.ru");

    private final UserDto firstUserDto = new UserDto(1L, "Артём", "artyom@gmail.com");
    private final UserDto secondUserDto = new UserDto(2L, "Владимир", "vladimir@mail.ru");

    @Test
    void getAllUsersSuccess() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(firstUser, secondUser));

        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(firstUser.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(firstUser.getName()), String.class))
                .andExpect(jsonPath("$[0].email", is(firstUser.getEmail()), String.class))
                .andExpect(jsonPath("$[1].id", is(secondUser.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(secondUser.getName()), String.class))
                .andExpect(jsonPath("$[1].email", is(secondUser.getEmail()), String.class));
    }

    @Test
    void getUserByIdSuccess() throws Exception {
        when(userService.getUserById(1L)).thenReturn(firstUser);

        mvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUser.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUser.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUser.getEmail()), String.class));
    }

    @Test
    void createUserSuccess() throws Exception {
        when(userService.createUser(any(UserEntity.class))).thenReturn(firstUser);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(firstUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUser.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUser.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUser.getEmail()), String.class));
    }

    @Test
    void updateUserSuccess() throws Exception {
        when(userService.updateUser(any(Long.class), any())).thenReturn(firstUser);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(firstUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUser.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUser.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUser.getEmail()), String.class));
    }

    @Test
    void deleteUserSuccess() throws Exception {
        mvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Пользователь с id = 1 удалён!")));
    }
}
