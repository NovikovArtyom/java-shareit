package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> userStorage = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.values();
    }

    @Override
    public User getUserById(Long userId) {
        return userStorage.get(userId);
    }
}
