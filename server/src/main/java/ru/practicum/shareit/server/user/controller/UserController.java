package ru.practicum.shareit.server.user.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.user.dto.UpdatedUserDto;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.dto.UserMapper;
import ru.practicum.shareit.server.user.model.UserEntity;
import ru.practicum.shareit.server.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

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
    public UserDto deleteUser(@PathVariable Long userId) {
        return UserMapper.toUserDto(userService.deleteUser(userId));
    }
}
