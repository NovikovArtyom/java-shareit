package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class BookingControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private UserEntity user;
    private UserEntity owner;
    private ItemEntity item;
    private BookingEntity booking;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setName("Test User");
        user.setEmail("testuser@example.com");
        user = userRepository.save(user);

        owner = new UserEntity();
        owner.setName("Item Owner");
        owner.setEmail("owner@example.com");
        owner = userRepository.save(owner);

        item = new ItemEntity();
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        booking = new BookingEntity();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingRepository.save(booking);
    }

    @Test
    void testGetBookingsByBooker() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", user.getId().toString());

        ResponseEntity<List<BookingResponseDto>> response = restTemplate.exchange(
                "/bookings",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getId()).isEqualTo(booking.getId());
    }

    @Test
    void testGetBookingsByOwner() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", owner.getId().toString());

        ResponseEntity<List<BookingResponseDto>> response = restTemplate.exchange(
                "/bookings/owner",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getId()).isEqualTo(booking.getId());
    }

    @Test
    void testGetBookingById() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", user.getId().toString());

        ResponseEntity<BookingResponseDto> response = restTemplate.exchange(
                "/bookings/" + booking.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                BookingResponseDto.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getId()).isEqualTo(booking.getId());
    }

    @Test
    void testAddBooking() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", user.getId().toString());

        BookingDto bookingDto = new BookingDto(null, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), item.getId(), null, null);

        ResponseEntity<BookingResponseDto> response = restTemplate.exchange(
                "/bookings",
                HttpMethod.POST,
                new HttpEntity<>(bookingDto, headers),
                BookingResponseDto.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    void testApproveBooking() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", owner.getId().toString());

        ResponseEntity<BookingResponseDto> response = restTemplate.exchange(
                "/bookings/" + booking.getId() + "?approved=true",
                HttpMethod.PATCH,
                new HttpEntity<>(headers),
                BookingResponseDto.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getStatus()).isEqualTo(BookingStatus.APPROVED);
    }
}
