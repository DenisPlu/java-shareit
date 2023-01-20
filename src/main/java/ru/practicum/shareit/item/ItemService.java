package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAllByOwner(Long ownerId);
    Item get(Long id);
    List <ItemDto> searchInNameAndDescription(String text);
    ItemDto create(ItemDto item, Long ownerId);
    ItemDto update(Long id, Item item, Long ownerId);
    void delete(Long id);
}
