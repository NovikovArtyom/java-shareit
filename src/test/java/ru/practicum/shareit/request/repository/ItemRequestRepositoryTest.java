package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.UserEntity;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private EntityManager em;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void setUp() {
        user1 = new UserEntity();
        user1.setName("Артём");
        user1.setEmail("artyom@gmail.com");
        em.persist(user1);

        user2 = new UserEntity();
        user2.setName("Владимир");
        user2.setEmail("vladimir@mail.ru");
        em.persist(user2);

        em.flush();
    }

    @Test
    void getAllByRequester_IdSuccess() {
        ItemRequest request1 = createAndSaveItemRequest("Нужна дрель", user1);
        ItemRequest request2 = createAndSaveItemRequest("Нужна машина", user1);

        List<ItemRequest> requests = itemRequestRepository.getAllByRequester_Id(user1.getId());

        assertThat(requests).hasSize(2).containsExactlyInAnyOrder(request1, request2);
    }

    @Test
    void getAllByRequester_IdNotOrderByCreatedAscSuccess() {
        ItemRequest request1 = createAndSaveItemRequest("Нужна дрель", user1);
        ItemRequest request2 = createAndSaveItemRequest("Нужна машина", user2);

        Page<ItemRequest> page = itemRequestRepository.getAllByRequester_IdNotOrderByCreatedAsc(user1.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1).containsExactly(request2);
    }

    private ItemRequest createAndSaveItemRequest(String description, UserEntity requester) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(description);
        itemRequest.setRequester(requester);
        itemRequest.setCreated(LocalDateTime.now());
        em.persist(itemRequest);
        em.flush();
        return itemRequest;
    }
}
