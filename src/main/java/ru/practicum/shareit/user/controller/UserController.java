package ru.practicum.shareit.user.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return UserMapper.toUserDto(userService.getUserById(userId));
    }

    @PostMapping
    public UserDto createUser(@RequestBody @Validated UserDto userDto) {
        UserEntity user = UserMapper.fromUserDtoToUserEntity(userDto);
        return UserMapper.toUserDto(userService.createUser(user));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UpdatedUserDto updatedUserDto) {
        return UserMapper.toUserDto(userService.updateUser(userId, updatedUserDto));
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return String.format("Пользователь с id = %d удалён!", userId);
    }
}
