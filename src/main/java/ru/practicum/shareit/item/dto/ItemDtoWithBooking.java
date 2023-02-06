package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDtoMin;
import ru.practicum.shareit.item.comment.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoWithBooking {
    Long id;
    @NotBlank
    @NonNull
    @Size(max = 50)
    String name;
    @NotBlank
    @NonNull
    @Size(max = 200)
    String description;
    boolean available;
    BookingDtoMin lastBooking;
    BookingDtoMin nextBooking;
    List<CommentDto> comments;
}
