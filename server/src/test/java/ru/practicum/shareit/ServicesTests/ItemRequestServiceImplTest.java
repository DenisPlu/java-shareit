package ru.practicum.shareit.ServicesTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
public class ItemRequestServiceImplTest {
    private final EntityManager em;
    private final ItemRequestServiceImpl itemRequestService;
    private final UserService userService;
    private User user1;
    private User user2;
    ItemRequest itemRequest1;
    ItemRequest itemRequest2;

    @BeforeEach
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void setUp() {
        user1 = new User( 1L, "User1", "user1@mail.com");
        user2 = new User( 12L, "User2", "user2@mail.com");
        userService.create(user1);
        userService.create(user2);
        itemRequest1 = new ItemRequest(
                1L,
                "???????????? ??????????1",
                null,
                LocalDateTime.now()
        );
        itemRequest2 = new ItemRequest(
                2L,
                "???????????? ??????????2",
                null,
                LocalDateTime.now()
        );
        itemRequestService.create(itemRequest1, 1L);
        itemRequestService.create(itemRequest2, 2L);
    }

    @Test
    @DirtiesContext
    void GetAllWithCorrectRequesterId() {
        TypedQuery<ItemRequest> query = em.createQuery("SELECT ir FROM ItemRequest ir WHERE ir.requesterId <> :requesterId ORDER BY ir.id DESC", ItemRequest.class);
        List<ItemRequest> itemRequests = query.setParameter("requesterId", itemRequest2.getRequesterId()).getResultList();
        assertThat(itemRequests.size(), equalTo(1));
        assertThat(itemRequests.get(0).getDescription(), equalTo(itemRequest1.getDescription()));
    }

    @Test
    @DirtiesContext
    void CreateWithWrongUserId() {
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            itemRequestService.create(itemRequest1, 99L);
        });
        Assertions.assertEquals("404 NOT_FOUND \"???? ?????????????????? ?????????? User\"", thrown.getMessage());
    }
}
