package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.LastAndNextBookingDto;
import ru.practicum.shareit.item.comments.CommentResponseDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDtoForItemRequest> json;

    @Autowired
    private JacksonTester<ItemDto> jsonDto;


    @Test
    void testItemDtoForItemRequest() throws Exception {
        ItemDtoForItemRequest itemDtoForItemRequest = new ItemDtoForItemRequest(
                1L,
                "Дрель",
                "Мощная дрель",
                true,
                2L
        );

        JsonContent<ItemDtoForItemRequest> result = json.write(itemDtoForItemRequest);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Мощная дрель");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }

    @Test
    void testItemDto() throws Exception {
        UserDto owner = new UserDto(1L, "Артём", "artyom@gmail.com");
        LastAndNextBookingDto lastBooking = new LastAndNextBookingDto(1L, 4L);
        LastAndNextBookingDto nextBooking = new LastAndNextBookingDto(4L, 5L);
        CommentResponseDto comment = new CommentResponseDto(1L, "Отличная вещь!", "Артём", LocalDateTime.of(2023, 6, 25, 18, 0));
        ItemDto itemDto = new ItemDto(
                1L,
                "Дрель",
                "Мощная дрель",
                true,
                owner,
                2L,
                lastBooking,
                nextBooking,
                List.of(comment)
        );

        JsonContent<ItemDto> result = jsonDto.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Мощная дрель");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("Артём");
        assertThat(result).extractingJsonPathStringValue("$.owner.email").isEqualTo("artyom@gmail.com");
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(4);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(4);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(5);
        assertThat(result).extractingJsonPathArrayValue("$.comments").hasSize(1);
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("Отличная вещь!");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo("Артём");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].created").isEqualTo("2023-06-25T18:00:00");
    }
}
