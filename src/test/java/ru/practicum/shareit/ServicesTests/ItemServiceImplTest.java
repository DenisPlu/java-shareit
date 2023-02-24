package ru.practicum.shareit.ServicesTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
public class ItemServiceImplTest {
    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserService userService;

    private final BookingServiceImpl bookingService;

    private User user1;
    private User user2;
    ItemDto itemDto1;
    ItemDto itemDto2;
    ItemDto itemDto3;

    @BeforeEach
    void setUp() {
        user1 = new User( 1L, "User1", "user1@mail.com");
        user2 = new User( 2L, "User2", "user2@mail.com");
        userService.create(user1);
        userService.create(user2);

        itemDto1 = new ItemDto(
                1L,
                "Дрель",
                "Аккумуляторная дрель",
                true,
                null
        );
        itemDto2 = new ItemDto(
                2L,
                "Дрель2",
                "Аккумуляторная дрель2",
                true,
                null
                );
        itemDto3 = new ItemDto(
                3L,
                "Дрель3",
                "Аккумуляторная дрель3",
                true,
                null
        );
        itemService.create(itemDto1, 1L);
        itemService.create(itemDto2, 1L);
        itemService.create(itemDto3, 2L);
    }

    @Test
    @DirtiesContext
    void GetAllByOwnerWithCorrectRequesterIdWithoutBooking() {
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.owner = :ownerId ORDER BY i.id ASC", Item.class);
        List<Item> items = query.setParameter("ownerId", 1L).getResultList();
        assertThat(items.size(), equalTo(2));
        assertThat(items.get(0).getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(items.get(0).getOwner(), equalTo(1L));
    }

    @Test
    @DirtiesContext
    void GetAllByOwnerWithCorrectRequesterIdWithBooking() {
        Booking booking1 = new Booking(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 2L, 1L, BookingStatus.WAITING);
        Booking booking2 = new Booking(2L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 2L, 2L, BookingStatus.APPROVED);
        bookingService.create(booking1, 2L);
        bookingService.create(booking2, 2L);

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.owner = :ownerId ORDER BY i.id ASC", Item.class);
        List<Item> items = query.setParameter("ownerId", 1L).getResultList();
        assertThat(items.size(), equalTo(2));
        assertThat(items.get(0).getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(items.get(0).getOwner(), equalTo(1L));
    }

    @Test
    void CreateWithWrongOwnerId() {
        EntityNotFoundException thrown = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            itemService.create(itemDto1, 99L);
        });
        Assertions.assertEquals("Unable to find ru.practicum.shareit.user.User with id 99", thrown.getMessage());
    }

    @Test
    @DirtiesContext
    void searchInNameAndDescription() {
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', :text, '%'))" +
                " OR UPPER(i.description) LIKE UPPER(CONCAT('%', :text, '%'))) AND (i.available) LIKE 'true'", Item.class);
        List<Item> items = query.setParameter("text", "Аккумуляторная").getResultList();
        assertThat(items.size(), equalTo(3));
        assertThat(items.get(0).getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(items.get(0).getOwner(), equalTo(1L));
    }
}
