package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class ItemRequestServiceImplTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final EntityManager em;

    private UserEntity user1;
    private UserEntity user2;
    private UserEntity savedUser1;
    private UserEntity savedUser2;

    @BeforeEach
    void setUp() {
        user1 = new UserEntity(null, "Артём", "artyom@gmail.com");
        user2 = new UserEntity(null, "Владимир", "vladimir@mail.ru");
        savedUser1 = userService.createUser(user1);
        savedUser2 = userService.createUser(user2);
    }

    @Test
    void getItemRequestByUserIdSuccess() {
        ItemRequest itemRequest = createAndSaveItemRequest(savedUser1.getId(), "Нужна дрель");
        TypedQuery<ItemRequest> query = em.createQuery("Select ir from ItemRequest ir where ir.requester.id = :userId",
                ItemRequest.class);
        List<ItemRequest> receivedItemRequest = query.setParameter("userId", savedUser1.getId()).getResultStream()
                .collect(Collectors.toList());
        assertThat(receivedItemRequest.size(), equalTo(1));
        verifyItemRequest(receivedItemRequest.get(0), itemRequest);
    }

    @Test
    void getAllItemRequestSuccess() {
        ItemRequest firstItemRequest = createAndSaveItemRequest(savedUser1.getId(), "Нужна дрель");
        ItemRequest secondItemRequest = createAndSaveItemRequest(savedUser2.getId(), "Нужна машина");

        TypedQuery<ItemRequest> query = em.createQuery("Select ir from ItemRequest ir where not ir.requester.id = :userId",
                ItemRequest.class);
        List<ItemRequest> receivedItemRequest = query.setParameter("userId", user2.getId()).getResultStream()
                .collect(Collectors.toList());
        assertThat(receivedItemRequest.size(), equalTo(1));
        verifyItemRequest(receivedItemRequest.get(0), firstItemRequest);
    }

    @Test
    void getItemRequestByRequestIdSuccess() {
        ItemRequest itemRequest = new ItemRequest(null, "Нужна дрель", null, null,
                Collections.emptyList());
        ItemRequest savedItemRequest = itemRequestService.createItemRequest(user1.getId(), itemRequest);

        TypedQuery<ItemRequest> query = em.createQuery("Select ir from ItemRequest ir where ir.id = :requestId",
                ItemRequest.class);
        ItemRequest receivedItemRequest = query.setParameter("requestId", savedItemRequest.getId()).getSingleResult();
        verifyItemRequest(receivedItemRequest, savedItemRequest);
    }

    @Test
    void createItemRequestSuccess() {
        ItemRequest itemRequest = createAndSaveItemRequest(savedUser1.getId(), "Нужна дрель");

        TypedQuery<ItemRequest> query = em.createQuery("Select ir from ItemRequest ir where ir.description = :description",
                ItemRequest.class);
        ItemRequest receivedItemRequest = query.setParameter("description", itemRequest.getDescription()).getSingleResult();

        verifyItemRequest(receivedItemRequest, itemRequest);
    }

    private ItemRequest createAndSaveItemRequest(Long userId, String description) {
        ItemRequest itemRequest = new ItemRequest(null, description, null, null, Collections.emptyList());
        itemRequestService.createItemRequest(userId, itemRequest);
        return itemRequest;
    }

    private void verifyItemRequest(ItemRequest received, ItemRequest expected) {
        assertThat(received.getId(), notNullValue());
        assertThat(received.getDescription(), equalTo(expected.getDescription()));
        assertThat(received.getRequester(), equalTo(expected.getRequester()));
        assertThat(received.getCreated(), equalTo(expected.getCreated()));
    }
}
