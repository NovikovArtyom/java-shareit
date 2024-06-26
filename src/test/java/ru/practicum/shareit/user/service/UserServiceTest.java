package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.DuplicateUserException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class UserServiceTest {
    private final UserService userService;
    private final UserRepository userRepository;
    private final EntityManager em;

    private UserEntity user;
    private UserEntity savedUser;
    private UserEntity anotherUser;
    private UserEntity savedAnotherUser;

    @BeforeEach
    void setUp() {
        user = new UserEntity(null, "Артём", "artyom@gmail.com");
        savedUser = userService.createUser(user);
        anotherUser = new UserEntity(null, "Владимир", "vladimir@mail.ru");
        savedAnotherUser = userService.createUser(anotherUser);
    }

    @Test
    void testGetUserById() {
        UserEntity foundUser = userService.getUserById(savedUser.getId());
        assertThat(foundUser, equalTo(savedUser));
    }

    @Test
    void testCreateUser() {
        UserEntity newUser = new UserEntity(null, "Иван", "ivan@gmail.com");
        UserEntity savedNewUser = userService.createUser(newUser);
        assertThat(savedNewUser.getId(), notNullValue());
        assertThat(savedNewUser.getName(), equalTo(newUser.getName()));
        assertThat(savedNewUser.getEmail(), equalTo(newUser.getEmail()));
    }

    @Test
    void testGetAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        assertThat(users).hasSize(2);
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        UserEntity duplicateEmailUser = new UserEntity(null, "Дмитрий", "artyom@gmail.com");
        assertThatThrownBy(() -> userService.createUser(duplicateEmailUser))
                .isInstanceOf(DuplicateUserException.class)
                .hasMessageContaining("Данный пользователь уже зарегистрирован!");
    }

    @Test
    void testUpdateUser() {
        UpdatedUserDto updatedUserDto = new UpdatedUserDto("Артём", "newemail@gmail.com");

        UserEntity updatedUser = userService.updateUser(savedUser.getId(), updatedUserDto);
        assertThat(updatedUser.getName(), equalTo(updatedUserDto.getName()));
        assertThat(updatedUser.getEmail(), equalTo(updatedUserDto.getEmail()));
    }

    @Test
    void testUpdateUserWithDuplicateEmail() {
        UpdatedUserDto updatedUserDto = new UpdatedUserDto("Владимир", "vladimir@mail.ru");

        assertThatThrownBy(() -> userService.updateUser(savedUser.getId(), updatedUserDto))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("Данный email занят!");
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(savedUser.getId());
        assertThatThrownBy(() -> userService.getUserById(savedUser.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Пользователь с данным ID не найден!");
    }

    @Test
    void testDeleteUserNotFound() {
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Пользователь не зарегистрирован!");
    }
}
