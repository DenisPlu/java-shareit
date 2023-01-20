package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                Boolean.parseBoolean(item.getAvailable())
        );
    }

    public static Item toItemFromDto(Long id, ItemDto itemDto, Long ownerId) {
        return new Item(
                id,
                itemDto.getName(),
                itemDto.getDescription(),
                String.valueOf(itemDto.isAvailable()),
                ownerId
        );
    }

}
