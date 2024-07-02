package ru.practicum.shareit.server.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.UserEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class BookingMapperTest {

    @Test
    void toBookingDtoSuccess() {
        UserEntity bookerEntity = new UserEntity(1L, "Артём", "artyom@gmail.com");
        ItemEntity itemEntity = new ItemEntity(1L, "Дрель", "Дрель Мокито", true, bookerEntity, null);
        BookingEntity bookingEntity = new BookingEntity(1L,
                LocalDateTime.of(2024, 12, 10, 10, 0),
                LocalDateTime.of(2024, 12, 12, 10, 0),
                itemEntity,
                bookerEntity,
                BookingStatus.WAITING);

        BookingResponseDto bookingDto = BookingMapper.toBookingDto(bookingEntity);

        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStart())
                .isEqualTo(LocalDateTime.of(2024, 12, 10, 10, 0));
        assertThat(bookingDto.getEnd())
                .isEqualTo(LocalDateTime.of(2024, 12, 12, 10, 0));
        assertThat(bookingDto.getItem()).isEqualTo(ItemMapper.toItemResponseDto(itemEntity));
        assertThat(bookingDto.getBooker()).isEqualTo(UserMapper.toUserResponseDto(bookerEntity));
        assertThat(bookingDto.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void toBookingEntitySuccess() {
        BookingDto bookingDto = new BookingDto(1L,
                LocalDateTime.of(2024, 12, 10, 10, 0),
                LocalDateTime.of(2024, 12, 12, 10, 0),
                1L,
                1L,
                BookingStatus.WAITING);

        BookingEntity bookingEntity = BookingMapper.toBookingEntity(bookingDto);

        assertThat(bookingEntity.getId()).isEqualTo(1L);
        assertThat(bookingEntity.getStart())
                .isEqualTo(LocalDateTime.of(2024, 12, 10, 10, 0));
        assertThat(bookingEntity.getEnd())
                .isEqualTo(LocalDateTime.of(2024, 12, 12, 10, 0));
        assertThat(bookingEntity.getItem()).isNull();
        assertThat(bookingEntity.getBooker()).isNull();
    }

    @Test
    void toLastAndNextBookingDtoSuccess() {
        BookingEntity bookingEntity = new BookingEntity(1L,
                LocalDateTime.of(2024, 12, 10, 10, 0),
                LocalDateTime.of(2024, 12, 12, 10, 0),
                null,
                new UserEntity(1L, "Артём", "artyom@gmail.com"),
                BookingStatus.WAITING);

        LastAndNextBookingDto lastAndNextBookingDto = BookingMapper.toLastAndNextBookingDto(bookingEntity);

        assertThat(lastAndNextBookingDto.getId()).isEqualTo(1L);
        assertThat(lastAndNextBookingDto.getBookerId()).isEqualTo(1L);
    }
}
