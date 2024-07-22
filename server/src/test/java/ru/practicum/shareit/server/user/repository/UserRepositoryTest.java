package ru.practicum.shareit.server.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.server.user.model.UserEntity;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void setUp() {
        user1 = new UserEntity(null, "Артём", "artyom@gmail.com");
        em.persist(user1);

        user2 = new UserEntity(null, "Владимир", "vladimir@mail.ru");
        em.persist(user2);

        em.flush();
    }

    @Test
    void existsByEmailSuccess() {
        boolean exists = userRepository.existsByEmail("artyom@gmail.com");
        assertThat(exists).isTrue();

        boolean doesNotExist = userRepository.existsByEmail("nonexistent@gmail.com");
        assertThat(doesNotExist).isFalse();
    }

    @Test
    void findByEmailSuccess() {
        UserEntity foundUser = userRepository.findByEmail("artyom@gmail.com");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("Артём");

        UserEntity notFoundUser = userRepository.findByEmail("nonexistent@gmail.com");
        assertThat(notFoundUser).isNull();
    }
}
