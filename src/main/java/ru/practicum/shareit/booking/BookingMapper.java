package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public final class BookingMapper {
    public static BookingDto toItemDto(Booking booking) {
        return new BookingDto(
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker(),
                booking.getItem(),
                booking.getStatus()
        );
    }

    public static Booking toBookingFromDto(Long id, BookingDto bookingDto) {
        return new Booking(
                id,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getBooker(),
                bookingDto.getItem(),
                bookingDto.getStatus()
        );
    }
}
