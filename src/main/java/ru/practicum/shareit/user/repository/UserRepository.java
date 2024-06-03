package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);

    UserEntity findByEmail(String email);
}