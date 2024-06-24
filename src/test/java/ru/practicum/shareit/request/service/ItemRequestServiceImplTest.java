package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final EntityManager em;

    @Test
    void createItemRequest() {
        UserEntity user = new UserEntity(null, "Артём", "artyom@gmail.com");
        UserEntity createdUser = userService.createUser(user);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Нужна дрель");
        itemRequestService.createItemRequest(createdUser.getId(), itemRequest);

        TypedQuery<ItemRequest> query = em.createQuery("Select ir from ItemRequest ir where ir.description = :description",
                ItemRequest.class);
        ItemRequest receivedItemRequest = query.setParameter("description", itemRequest.getDescription()).getSingleResult();

        assertThat(receivedItemRequest.getId(), notNullValue());
        assertThat(receivedItemRequest.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(receivedItemRequest.getRequester(), equalTo(itemRequest.getRequester()));
        assertThat(receivedItemRequest.getCreated(), equalTo(itemRequest.getCreated()));
    }


    private ItemRequest makeItemRequest(String description, UserEntity user, LocalDateTime date) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(description);
        itemRequest.setRequester(user);
        itemRequest.setCreated(date);
        return itemRequest;
    }
}
