package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoMin;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.dto.ItemDtoMin;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RequiredArgsConstructor
public final class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                Boolean.parseBoolean(item.getAvailable()),
                item.getRequestId()
        );
    }

    public static ItemDtoWithBooking toItemDtoWithBooking(Item item, BookingDtoMin lastBooking, BookingDtoMin nextBooking, List<CommentDto> comments) {
        return new ItemDtoWithBooking(
                item.getId(),
                item.getName(),
                item.getDescription(),
                Boolean.parseBoolean(item.getAvailable()),
                lastBooking,
                nextBooking,
                comments
        );
    }

    public static ItemDtoMin toItemDtoMin(Item item) {
        return new ItemDtoMin(
                item.getId(),
                item.getName()
        );
    }

    public static ItemDtoForRequest toItemDtoForRequest(Item item) {
        return new ItemDtoForRequest(
                item.getId(),
                item.getName(),
                item.getDescription(),
                Boolean.parseBoolean(item.getAvailable()),
                item.getRequestId()
        );
    }

    public static Item toItemFromDto(ItemDto itemDto, Long ownerId) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                String.valueOf(itemDto.isAvailable()),
                ownerId,
                itemDto.getRequestId()
        );
    }

}
