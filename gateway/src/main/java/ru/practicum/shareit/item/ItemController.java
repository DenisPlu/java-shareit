package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(value = USER_ID) Long ownerId) {
        log.info("Get all items of an owner id {}", ownerId);
        return itemClient.getAllByOwner(ownerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id, @RequestHeader(value = USER_ID) Long userId) {
        log.info("Get an item with id {} from user {}", id, userId);
            return itemClient.get(id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchInNameAndDescription(@RequestParam String text) {
        log.info("Search an item with text {} in name or description", text);
        return itemClient.searchInNameAndDescription(text);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemDto item, @RequestHeader(value = USER_ID) Long ownerId) {
        log.info("Create a new item: {} by user with id {}", item, ownerId);
        return itemClient.create(item, ownerId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody ItemDtoForUpdate itemDto, @RequestHeader(value = USER_ID) Long ownerId) {
        log.info("Update an item with id: {}, ownerId: {}, item: {}", id, ownerId, itemDto);
        return itemClient.update(id, itemDto, ownerId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete an item with id: {}", id);
        itemClient.delete(id);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId, @RequestBody @Valid Comment comment, @RequestHeader(value = USER_ID) Long authorId) {
        log.info("Create a new Comment {} to item: {} by user with ud {}", comment, itemId, authorId);
        return itemClient.createComment(itemId, comment, authorId);
    }
}
