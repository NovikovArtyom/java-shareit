package ru.practicum.shareit.server.request.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDtoRequest {
    @NotNull
    private String description;
}
