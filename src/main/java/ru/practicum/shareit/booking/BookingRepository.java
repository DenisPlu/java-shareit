package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT * FROM BOOKINGS WHERE (BOOKER_ID = ?) ORDER BY ID DESC LIMIT ? OFFSET ?", nativeQuery = true)
    List<Booking> findByBookerIdWithPagination(Long ownerId, Integer from, Integer size);

    @Query(value = "SELECT * FROM BOOKINGS WHERE (BOOKER_ID = ?) ORDER BY ID DESC OFFSET ?", nativeQuery = true)
    List<Booking> findByBookerIdWithoutPagination(Long ownerId, Integer from);

    List<Booking>  findByItemId(Long itemId);
    @Query(value = "SELECT * FROM BOOKINGS WHERE (ITEM_ID = ?) ORDER BY ID DESC LIMIT ? OFFSET ?", nativeQuery = true)
    List<Booking>  findByItemIdWithPagination(Long itemId, Integer from, Integer size);

    @Query(value = "SELECT * FROM BOOKINGS WHERE (ITEM_ID = ?) ORDER BY ID DESC OFFSET ?", nativeQuery = true)
    List<Booking>  findByItemIdWithoutPagination(Long itemId, Integer from);
}
