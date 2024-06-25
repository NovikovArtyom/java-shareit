package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingResponseDto> json;

    @Test
    void testBookingResponseDto() throws Exception {
        ItemResponseDto item = new ItemResponseDto(1L, "Дрель");
        UserResponseDto booker = new UserResponseDto(1L);
        BookingResponseDto bookingResponseDto = new BookingResponseDto(
                1L,
                LocalDateTime.of(2024, 12, 10, 22, 2),
                LocalDateTime.of(2024, 12, 12, 22, 2),
                item,
                booker,
                BookingStatus.WAITING
        );

        JsonContent<BookingResponseDto> result = json.write(bookingResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2024-12-10T22:02:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-12-12T22:02:00");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}