package ru.practicum.shareit.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User fromUserDtoToUserEntity(UserDto userDto) {
        return new User(
                null,
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static User fromUpdatedUserDtoToUserEntity(UpdatedUserDto updatedUserDto) {
        return new User(
                null,
                updatedUserDto.getName(),
                updatedUserDto.getEmail()
        );
    }
}
