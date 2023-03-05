package ru.practicum.shareit.ServicesTests;

import lombok.RequiredArgsConstructor;
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
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(SpringExtension.class)
public class BookingServiceImplTest {
    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserService userService;
    private final BookingService bookingService;
    ItemDto itemDto1;
    ItemDto itemDto2;
    ItemDto itemDto3;
    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "User1", "user1@mail.com");
        user2 = new User(2L, "User2", "user2@mail.com");
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
    void GetAllByOwnerWithCorrectRequesterId() {
        Booking booking1 = new Booking(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 2L, 1L, BookingStatus.WAITING);
        Booking booking2 = new Booking(2L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 2L, 2L, BookingStatus.WAITING);
        Booking booking3 = new Booking(3L, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), 2L, 2L, BookingStatus.WAITING);
        Booking booking4 = new Booking(4L, LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(6), 2L, 2L, BookingStatus.WAITING);
        Booking booking5 = new Booking(5L, LocalDateTime.now().plusDays(7), LocalDateTime.now().plusDays(8), 2L, 2L, BookingStatus.WAITING);
        System.out.println(bookingService.create(booking1, 2L));
        System.out.println(bookingService.create(booking2, 2L));
        bookingService.create(booking3, 2L);
        bookingService.create(booking4, 2L);
        bookingService.create(booking5, 2L);

        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.itemId = :itemId ORDER BY b.id ASC", Booking.class);
        List<Booking> bookings = query.setParameter("itemId", 2L).getResultList();
        assertThat(bookings.size(), equalTo(4));
        assertThat(bookings.get(0).getStatus(), equalTo(booking2.getStatus()));
        assertThat(bookings.get(0).getStart(), equalTo(booking2.getStart()));
    }
}
