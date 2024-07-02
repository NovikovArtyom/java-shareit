package ru.practicum.shareit.server.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.server.item.dto.ItemMapper;
import ru.practicum.shareit.server.booking.model.BookingEntity;
import ru.practicum.shareit.server.user.dto.UserMapper;


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

    public static LastAndNextBookingDto toLastAndNextBookingDto(BookingEntity bookingEntity) {
        return new LastAndNextBookingDto(
                bookingEntity.getId() != null ? bookingEntity.getId() : null,
                bookingEntity.getBooker() != null ? bookingEntity.getBooker().getId() : null
        );
    }
}
