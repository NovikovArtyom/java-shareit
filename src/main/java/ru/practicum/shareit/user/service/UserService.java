package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Collection<UserDto> getAllUsers() {
        return repository.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        return UserMapper.toUserDto(repository.getUserById(userId));
    }

    public UserDto createUser(UserDto userDto) {
        User createdUser = UserMapper.toUserEntity(repository.createUser());
    }
}
