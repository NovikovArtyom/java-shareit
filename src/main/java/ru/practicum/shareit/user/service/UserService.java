package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.model.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> getAllUsers();

    UserEntity getUserById(Long userId);

    UserEntity createUser(UserEntity user);

    UserEntity updateUser(Long userId, UpdatedUserDto updatedUserDto);

    void deleteUser(Long userId);
}
