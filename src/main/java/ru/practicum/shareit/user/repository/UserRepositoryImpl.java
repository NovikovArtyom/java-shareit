package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.DuplicateUserException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userStorage = new HashMap<>();
    private static Long userId = 0L;

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.values();
    }

    @Override
    public User getUserById(Long userId) {
        return userStorage.get(userId);
    }

    @Override
    public User createUser(User user) {
        if (!userStorage.containsValue(user)) {
            List<User> duplicateEmail = userStorage.values().stream()
                    .filter(item -> item.getEmail().equals(user.getEmail()))
                    .collect(Collectors.toList());
            if (duplicateEmail.isEmpty()) {
                userId = idGenerator();
                user.setId(userId);
                userStorage.put(userId, user);
                return userStorage.get(userId);
            } else {
                throw new DuplicateEmailException("Пользователь с данным email уже зарегистрирован!");
            }
        } else {
            throw new DuplicateUserException("Добавляемый пользователь уже зарегистрирован!");
        }
    }

    @Override
    public User updateUser(Long userId, User user) {
        User userToUpdate = userStorage.get(userId);
        if (userToUpdate != null) {
            if (user.getEmail() != null) {
                List<User> duplicateEmail = userStorage.values().stream()
                        .filter(item -> item.getEmail().equals(user.getEmail()))
                        .filter(item -> item != userToUpdate)
                        .collect(Collectors.toList());
                if (duplicateEmail.isEmpty()) {
                    userToUpdate.setEmail(user.getEmail());
                } else {
                    throw new DuplicateEmailException("Обновляемый email уже зарегистрирован!");
                }
            }
            if (user.getName() != null) {
                userToUpdate.setName(user.getName());
            }
            userStorage.put(userId, userToUpdate);
            return userToUpdate;
        } else {
            throw new UserNotFoundException("Обновляемый пользователь не зарегистрирован!");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        User userToDelete = userStorage.get(userId);
        if (userToDelete != null) {
            userStorage.remove(userId);
        } else {
            throw new UserNotFoundException("Удаляемый пользователь не зарегистрирован!");
        }
    }

    private Long idGenerator() {
        userId++;
        return userId;
    }
}
