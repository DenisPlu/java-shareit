package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForUpdate;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemDtoWithBooking> getAllByOwner(Long ownerId);

    ItemDtoWithBooking get(Long id, Long userId);

    List<ItemDto> searchInNameAndDescription(String text);

    ItemDto create(ItemDto item, Long ownerId);

    ItemDto update(Long id, ItemDtoForUpdate itemDto, Long ownerId);

    void delete(Long id);

    CommentDto createComment(Long itemId, Long authorId, Comment comment);
}