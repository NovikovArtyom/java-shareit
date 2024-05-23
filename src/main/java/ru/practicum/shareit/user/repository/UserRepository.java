package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;


public interface UserRepository {
    Collection<User> getAllUsers();

    User getUserById(Long userId);

    User createUser(User userEntity);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);
}