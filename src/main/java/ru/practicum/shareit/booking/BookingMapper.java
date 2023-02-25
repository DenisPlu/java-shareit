package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMin;
import ru.practicum.shareit.item.dto.ItemDtoMin;
import ru.practicum.shareit.user.UserDtoId;

@RequiredArgsConstructor
public final class BookingMapper {
    public static BookingDto toBookingDto(Booking booking, UserDtoId user, ItemDtoMin item) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                user,
                booking.getStatus(),
                item
        );
    }

    public static BookingDtoMin toBookingDtoMin(Booking booking) {
        return new BookingDtoMin(
                booking.getId(),
                booking.getBookerId()
        );
    }

    public static Booking toBookingFromDto(BookingDto bookingDto, Long bookerId) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookerId,
                bookingDto.getItem().getId(),
                bookingDto.getStatus()
        );
    }
}
