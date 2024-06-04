package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

@UtilityClass
public class BookingMapper {

    public static BookingResponseDto toBookingDto(BookingEntity bookingEntity) {
        return new BookingResponseDto(
                bookingEntity.getId(),
                bookingEntity.getStart(),
                bookingEntity.getEnd(),
                ItemMapper.toItemResponseDto(bookingEntity.getItem()),
                UserMapper.toUserResponseDto(bookingEntity.getBooker()),
                bookingEntity.getStatus()
        );
    }

    public static BookingEntity toBookingEntity(BookingDto bookingDto) {
        return new BookingEntity(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                null,
                null,
                null
        );
    }
}
