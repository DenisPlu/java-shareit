package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "SELECT * FROM bookings WHERE (booker_id = ?) ORDER BY id DESC LIMIT ? OFFSET ?", nativeQuery = true)
    List<Booking> findByBookerIdWithPagination(Long ownerId, Integer size, Integer from);

    List<Booking>  findByItemId(Long itemId);

    @Query(value = "SELECT * FROM bookings WHERE (item_id = ?) ORDER BY id DESC LIMIT ? OFFSET ?", nativeQuery = true)
    List<Booking>  findByItemIdWithPagination(Long itemId, Integer size, Integer from);

}
