package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForUpdate;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDtoWithBooking> getAll(@RequestHeader(value = USER_ID) Long ownerId) {
        log.info("Received a request to get all items of an owner id {}", ownerId);
        return itemService.getAllByOwner(ownerId);
    }

    @GetMapping("/{id}")
    public ItemDtoWithBooking get(@PathVariable Long id, @RequestHeader(value = USER_ID) Long userId) {
        log.info("Received a request to get an item with id {} from user {}", id, userId);
        try {
            return itemService.get(id, userId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item с данным id не существует");
        }
    }

    @GetMapping("/search")
    public List<ItemDto> searchInNameAndDescription(@RequestParam String text) {
        log.info("Received a request to search an item with text {} in name or description", text);
        return itemService.searchInNameAndDescription(text);
    }

    @PostMapping
    public ItemDto create(@RequestBody ItemDto item, @RequestHeader(value = USER_ID) Long ownerId) {
        log.info("Received a request to create a new item: {}", item);
        return itemService.create(item, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable Long id, @RequestBody ItemDtoForUpdate itemDto, @RequestHeader(value = USER_ID) Long ownerId) {
        log.info("Received a request to update an item with id: {}, ownerId: {}, item: {}", id, ownerId, itemDto);
        return itemService.update(id, itemDto, ownerId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Received a request to delete an item with id: {}", id);
        itemService.delete(id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId, @RequestBody Comment comment, @RequestHeader(value = USER_ID) Long authorId) {
        log.info("Received a request to create a new Comment to item: {}", itemId);
        return itemService.createComment(itemId, authorId, comment);
    }
}
