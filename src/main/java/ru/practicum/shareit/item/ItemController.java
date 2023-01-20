package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(value="X-Sharer-User-Id") Long ownerId) {
        log.info("Received a request to get all items of an owner id {}", ownerId);
        return itemService.getAllByOwner(ownerId);
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable Long id) {
        log.info("Received a request to get an item with id {}", id);
        return ItemMapper.toItemDto(itemService.get(id));
    }

    @GetMapping("/search")
    public List<ItemDto> searchInNameAndDescription(@RequestParam String text) {
        log.info("Received a request to search an item with text {} in name or description", text);
        return itemService.searchInNameAndDescription(text);
    }

    @PostMapping
    public ItemDto create(@RequestBody @Valid ItemDto item, @RequestHeader(value="X-Sharer-User-Id") Long ownerId){
        log.info("Received a request to create a new item: {}", item);
        return itemService.create(item, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable Long id, @RequestBody Item item, @RequestHeader(value="X-Sharer-User-Id") Long ownerId) {
        log.info("Received a request to update an item with id: {}, ownerId: {}, item: {}", id, ownerId, item);
        return itemService.update(id, item, ownerId);
    }

    @DeleteMapping("/{id}")
    public void delete (@PathVariable Long id) {
        log.info("Received a request to delete a user with id: {}", id);
        itemService.delete(id);
    }
}
