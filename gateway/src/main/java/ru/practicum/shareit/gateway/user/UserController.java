package ru.practicum.shareit.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.user.dto.UpdatedUserDto;
import ru.practicum.shareit.gateway.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public Object getAllUsers() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public Object getUserById(@PathVariable Long userId) {
        log.info("Get user by userId={}", userId);
        return userClient.getUserById(userId);
    }

    @PostMapping
    public Object createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Create new user");
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public Object updateUser(@PathVariable Long userId,
                             @RequestBody UpdatedUserDto updatedUserDto) {
        log.info("Update user with id={}", userId);
        return userClient.updateUser(userId, updatedUserDto);
    }

    @DeleteMapping("/{userId}")
    public Object deleteUser(@PathVariable Long userId) {
        log.info("Delete user with id={}", userId);
        return userClient.deleteUser(userId);
    }
}
