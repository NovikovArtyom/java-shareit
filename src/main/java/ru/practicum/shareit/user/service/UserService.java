package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long userId, UpdatedUserDto updatedUserDto);

    void deleteUser(Long userId);
}
