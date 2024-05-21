package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @NotBlank
    @Email
    private String email;
}
