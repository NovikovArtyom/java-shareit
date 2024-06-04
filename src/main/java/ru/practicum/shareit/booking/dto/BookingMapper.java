package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.BookingEntity;

@UtilityClass
public class BookingMapper {

    public static BookingDto toBookingDto(BookingEntity bookingEntity) {
        return new BookingDto(
                bookingEntity.getId(),
                bookingEntity.getStart(),
                bookingEntity.getEnd(),
                bookingEntity.getItem().getId(),
                bookingEntity.getBooker().getId(),
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
