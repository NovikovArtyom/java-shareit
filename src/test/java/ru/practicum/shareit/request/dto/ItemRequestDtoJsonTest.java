package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemRequestDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDtoResponse> responseJson;

    @Test
    void testItemRequestDtoResponse() throws Exception {
        ItemRequestDtoResponse itemRequestDtoResponse = new ItemRequestDtoResponse(
                1L,
                "Нужна дрель",
                new UserResponseDto(1L),
                LocalDateTime.of(2024, 12, 10, 22, 2)
        );
        JsonContent<ItemRequestDtoResponse> result = responseJson.write(itemRequestDtoResponse);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Нужна дрель");
        assertThat(result).extractingJsonPathNumberValue("$.requester.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2024-12-10T22:02:00");
    }
}
