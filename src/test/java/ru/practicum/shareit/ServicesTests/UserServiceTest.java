package ru.practicum.shareit.ServicesTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    private final EntityManager em;
    private final UserService userService;

    @Test
    void save() {
        User user1 = new User(1L, "Petr", "petr@gmail.com");
        userService.create(user1);

        // then
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user2 = query.setParameter("email", user1.getEmail())
                .getSingleResult();

        assertThat(user2.getId(), notNullValue());
        assertThat(user2.getName(), equalTo(user1.getName()));
        assertThat(user2.getEmail(), equalTo(user1.getEmail()));
    }
}
