package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    BookingService bookingService;
    @MockBean
    UserService userService;
    @MockBean
    ItemService itemService;

    private UserEntity user;
    private UserEntity owner;
    private ItemEntity item;
    private BookingEntity booking;

    @BeforeEach
    void setUp() {
        user = new UserEntity(1L, "Артём", "artyom@gmail.com");
        owner = new UserEntity(2L, "Владимир", "vladimir@mail.ru");
        item = new ItemEntity(1L, "Дрель", "Дрель Мокито", true, owner, null);
        booking = new BookingEntity(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(2), item, user, BookingStatus.WAITING);

        when(userService.createUser(user)).thenReturn(user);
        when(userService.createUser(owner)).thenReturn(owner);
        when(itemService.addItem(item.getId(), null, item)).thenReturn(item);
        when(bookingService.addBooking(user.getId(), booking, item.getId())).thenReturn(booking);
    }

    @Test
    void testGetBookingsByBooker() throws Exception {
        when(bookingService.getBookingsByBooker(user.getId(), "WAITING", 0, 10))
                .thenReturn(List.of(booking));

        mvc.perform(get("/bookings?state=WAITING&from=0&size=10")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(booking.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].end", is(booking.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(item.getName()), String.class))
                .andExpect(jsonPath("$[0].booker.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is("WAITING"), String.class));
    }

    @Test
    void testGetBookingsByOwner() throws Exception {
        when(bookingService.getBookingByOwner(owner.getId(), "WAITING", 0, 10))
                .thenReturn(List.of(booking));

        mvc.perform(get("/bookings/owner?state=WAITING&from=0&size=10")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(booking.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].end", is(booking.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(item.getName()), String.class))
                .andExpect(jsonPath("$[0].booker.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is("WAITING"), String.class));
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(user.getId(), booking.getId()))
                .thenReturn(booking);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(booking.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(item.getName()), String.class))
                .andExpect(jsonPath("$.booker.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.status", is("WAITING"), String.class));
    }

    @Test
    void testAddBooking() throws Exception {
        BookingDto bookingRequestDto = new BookingDto(
                null,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(4),
                item.getId(),
                null,
                null
        );

        when(bookingService.addBooking(any(), any(), any()))
                .thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .header("X-Sharer-User-Id", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(booking.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(item.getName()), String.class))
                .andExpect(jsonPath("$.booker.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.status", is("WAITING"), String.class));
    }

    @Test
    void testApproveBooking() throws Exception {
        when(bookingService.approveBooking(owner.getId(), booking.getId(), true))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(booking.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(booking.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(item.getName()), String.class))
                .andExpect(jsonPath("$.booker.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.status", is("WAITING"), String.class));
    }
}
