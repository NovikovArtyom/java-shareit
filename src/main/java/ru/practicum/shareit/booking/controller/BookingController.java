package ru.practicum.shareit.booking.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID = "X-Sharer-User-Id";

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingDto> getBookingsByBooker(@RequestHeader(USER_ID) Long userId,
                                                @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsByBooker(userId, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(@RequestHeader(USER_ID) Long userId,
                                               @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingByOwner(userId, state).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(USER_ID) Long userId, @PathVariable Long bookingId) {
        return BookingMapper.toBookingDto(bookingService.getBookingById(userId, bookingId));
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader(USER_ID) Long userId, @RequestBody @Validated BookingDto bookingDto) {
        Long itemId = bookingDto.getItemId();
        return BookingMapper.toBookingDto(
                bookingService.addBooking(userId, BookingMapper.toBookingEntity(bookingDto), itemId)
        );
    }

    @PostMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(USER_ID) Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam Boolean approved) {
        return BookingMapper.toBookingDto(bookingService.approveBooking(userId, bookingId, approved));
    }
}
