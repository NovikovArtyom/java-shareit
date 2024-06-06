package ru.practicum.shareit.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.UserEntity;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static UserEntity fromUserDtoToUserEntity(UserDto userDto) {
        return new UserEntity(
                userDto.getId() != null ? userDto.getId() : null,
                userDto.getName(),
                userDto.getEmail()
        );
    }

//    public static UserEntity fromUpdatedUserDtoToUserEntity(UpdatedUserDto updatedUserDto) {
//        return new UserEntity(
//                null,
//                updatedUserDto.getName(),
//                updatedUserDto.getEmail()
//        );
//    }

    public static UserResponseDto toUserResponseDto(UserEntity userEntity) {
        return new UserResponseDto(
                userEntity.getId()
        );
    }
}
