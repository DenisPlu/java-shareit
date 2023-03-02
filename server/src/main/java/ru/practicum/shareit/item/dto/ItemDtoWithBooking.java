package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDtoMin;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoWithBooking {
    Long id;
    String name;
    String description;
    boolean available;
    BookingDtoMin lastBooking;
    BookingDtoMin nextBooking;
    List<CommentDto> comments;
}
