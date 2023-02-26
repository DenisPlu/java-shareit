package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    List<BookingDto> getAllByOwner(BookingRequestState state, Long ownerId, Integer from, String size);

    List<BookingDto> getAllByUser(BookingRequestState state, Long ownerId, Integer from, String size);

    BookingDto get(Long id, Long userId);

    BookingDto create(Booking booking, Long ownerId);

    BookingDto update(Long id, Long ownerId, String approved);

    void delete(Long id);
}
