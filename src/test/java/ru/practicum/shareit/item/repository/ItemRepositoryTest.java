package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.user.model.UserEntity;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EntityManager em;

    private UserEntity user1;
    private UserEntity user2;
    private ItemEntity item1;
    private ItemEntity item2;

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

        item1 = new ItemEntity();
        item1.setName("Дрель");
        item1.setDescription("Мощная дрель");
        item1.setAvailable(true);
        item1.setOwner(user1);
        em.persist(item1);

        item2 = new ItemEntity();
        item2.setName("Машина");
        item2.setDescription("Старая машина");
        item2.setAvailable(true);
        item2.setOwner(user2);
        em.persist(item2);

        em.flush();
    }

    @Test
    void findAllByOwner_IdSuccess() {
        Page<ItemEntity> itemsPage = itemRepository.findAllByOwner_Id(user1.getId(), PageRequest.of(0, 10));

        List<ItemEntity> items = itemsPage.getContent();
        assertThat(items).hasSize(1).containsExactly(item1);
    }

    @Test
    void searchSuccess() {
        Page<ItemEntity> itemsPage = itemRepository.search("дрель", PageRequest.of(0, 10));

        List<ItemEntity> items = itemsPage.getContent();
        assertThat(items).hasSize(1).containsExactly(item1);

        itemsPage = itemRepository.search("машина", PageRequest.of(0, 10));

        items = itemsPage.getContent();
        assertThat(items).hasSize(1).containsExactly(item2);
    }
}
