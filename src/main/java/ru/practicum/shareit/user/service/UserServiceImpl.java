package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.DuplicateUserException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository repository) {
        this.userRepository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с данным ID не найден!"));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserEntity createUser(UserEntity user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new DuplicateUserException("Данный пользователь уже зарегистрирован!");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserEntity updateUser(Long userId, UpdatedUserDto updatedUserDto) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Обновляемый пользователь не найден!"));
        if (updatedUserDto.getEmail() != null) {
            Boolean duplicateEmail = userRepository.existsByEmail(updatedUserDto.getEmail());
            UserEntity userDuplicateEmail = userRepository.findByEmail(updatedUserDto.getEmail());
            if (!duplicateEmail || userDuplicateEmail == user) {
                user.setEmail(updatedUserDto.getEmail());
            } else {
                throw new DuplicateEmailException("Данный email занят!");
            }
        }
        if (updatedUserDto.getName() != null) {
            user.setName(updatedUserDto.getName());
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException("Пользователь не зарегистрирован!");
        }
    }
}
