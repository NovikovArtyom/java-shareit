package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.UserAccessException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class ItemServiceTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final EntityManager em;

    private UserEntity owner;
    private UserEntity savedOwner;
    private UserEntity anotherUser;
    private UserEntity savedAnotherUser;
    private ItemEntity item;
    private ItemEntity savedItem;
    private BookingEntity booking;
    private BookingEntity savedBooking;

    @BeforeEach
    void setUp() {
        owner = new UserEntity(null, "Артём", "artyom@gmail.com");
        savedOwner = userService.createUser(owner);
        anotherUser = new UserEntity(null, "Владимир", "vladimir@mail.ru");
        savedAnotherUser = userService.createUser(anotherUser);
        item = new ItemEntity(null, "Дрель", "Дрель Мокито", true, null, null);
        savedItem = itemService.addItem(savedOwner.getId(), null, item);
        booking = new BookingEntity(null, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                null, null, BookingStatus.WAITING);
        savedBooking = bookingService.addBooking(savedAnotherUser.getId(), booking, savedItem.getId());
    }

    @Test
    void getAllItemsSuccess() {
        List<ItemDto> items = itemService.getAllItems(savedOwner.getId(), 0, 10);
        assertThat(items).hasSize(1);
        verifyItem(items.get(0), savedItem);
    }

    @Test
    void getItemByIdSuccess() {
        ItemDto receivedItem = itemService.getItemById(savedOwner.getId(), savedItem.getId());
        verifyItem(receivedItem, savedItem);
    }

    @Test
    void addItemSuccess() {
        ItemEntity newItem = new ItemEntity(null, "Перфоратор", "Перфоратор Бош", true, null, null);
        ItemEntity savedNewItem = itemService.addItem(savedOwner.getId(), null, newItem);
        assertThat(savedNewItem.getId(), notNullValue());
        assertThat(savedNewItem.getName(), equalTo(newItem.getName()));
        assertThat(savedNewItem.getDescription(), equalTo(newItem.getDescription()));
        assertThat(savedNewItem.getAvailable(), equalTo(newItem.getAvailable()));
        assertThat(savedNewItem.getOwner(), equalTo(savedOwner));
    }

    @Test
    void updateItemSuccess() {
        UpdatedItemDto updatedItemDto = new UpdatedItemDto();
        updatedItemDto.setName("Аккумуляторная дрель");
        updatedItemDto.setDescription("Аккумуляторная дрель Мокито");
        updatedItemDto.setAvailable(false);

        ItemEntity updatedItem = itemService.updateItem(savedOwner.getId(), savedItem.getId(), updatedItemDto);
        assertThat(updatedItem.getName(), equalTo(updatedItemDto.getName()));
        assertThat(updatedItem.getDescription(), equalTo(updatedItemDto.getDescription()));
        assertThat(updatedItem.getAvailable(), equalTo(updatedItemDto.getAvailable()));
    }

    @Test
    void updateItemUserAccessException() {
        UpdatedItemDto updatedItemDto = new UpdatedItemDto();
        updatedItemDto.setName("Аккумуляторная дрель");

        assertThatThrownBy(() -> itemService.updateItem(savedAnotherUser.getId(), savedItem.getId(), updatedItemDto))
                .isInstanceOf(UserAccessException.class)
                .hasMessageContaining("Редактировать информацию о вещи может только ее владелец!");
    }

    private void verifyItem(ItemDto received, ItemEntity expected) {
        assertThat(received.getId(), notNullValue());
        assertThat(received.getName(), equalTo(expected.getName()));
        assertThat(received.getDescription(), equalTo(expected.getDescription()));
        assertThat(received.getAvailable(), equalTo(expected.getAvailable()));
        assertThat(received.getOwner().getId(), equalTo(expected.getOwner().getId()));
    }
}
