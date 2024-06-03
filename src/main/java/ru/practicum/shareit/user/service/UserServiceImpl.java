package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    public Collection<UserDto> getAllUsers() {
        return repository.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        return UserMapper.toUserDto(repository.getUserById(userId));
    }

    public UserDto createUser(UserDto userDto) {
        User createdUser = repository.createUser(UserMapper.fromUserDtoToUserEntity(userDto));
        return UserMapper.toUserDto(createdUser);
    }

    public UserDto updateUser(Long userId, UpdatedUserDto updatedUserDto) {
        User updatedUser = repository.updateUser(userId, UserMapper.fromUpdatedUserDtoToUserEntity(updatedUserDto));
        return UserMapper.toUserDto(updatedUser);
    }

    public void deleteUser(Long userId) {
        repository.deleteUser(userId);
    }
}
