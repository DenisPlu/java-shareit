package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemInMemoryStorage implements ItemStorage {
    private Long currentId = 1L;
    private final Map<Long, Item> itemMap = new HashMap<>();

    @Override
    public List<ItemDto> getAllByOwner(Long ownerId) {
        List<Item> itemList = new ArrayList<>(itemMap.values());
        List<Item> itemGetAllByOwnerList = itemList.stream().filter(g -> g.getOwner().equals(ownerId)).collect(Collectors.toList());
        List<ItemDto> itemDtoGetAllByOwnerList = new ArrayList<>();
        for (Item item: itemGetAllByOwnerList) {
            itemDtoGetAllByOwnerList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoGetAllByOwnerList;
    }

    @Override
    public Item get(Long id) {
        try {
            return itemMap.get(id);
        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item с данным id не существует");
        }
    }

    @Override
    public ItemDto create(ItemDto item, Long ownerId) {
        Item newItem = ItemMapper.toItemFromDto(currentId, item, ownerId);
        itemMap.put(currentId++, newItem);
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto update(Long id, Item item, Long ownerId) {
        if (Objects.equals(get(id).getOwner(), ownerId)) {
            if (Optional.ofNullable(item.getName()).isPresent()) {
                itemMap.get(id).setName(item.getName());
            }
            if (Optional.ofNullable(item.getDescription()).isPresent()) {
                itemMap.get(id).setDescription(item.getDescription());
            }
            Optional<String> isAvailable = Optional.ofNullable(item.getAvailable());
            if (isAvailable.isPresent()) {
                if (isAvailable.get().equals("true")) {
                    itemMap.get(id).setAvailable("true");
                } else {
                    itemMap.get(id).setAvailable("false");
                }
            }
            return ItemMapper.toItemDto(itemMap.get(id));
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Некорректный id Владельца item");
        }
    }

    @Override
    public List<ItemDto> searchInNameAndDescription(String text) {
        List<Item> itemListWithAvailableItems = itemMap.values().stream().filter(f -> f.getAvailable().equals("true")).collect(Collectors.toList());
        List<ItemDto> resultOfSearch = new ArrayList<>();
        if (!text.isEmpty()) {
            for (Item item : itemListWithAvailableItems) {
                if (item.getName().toLowerCase().contains(text.toLowerCase(Locale.ROOT)) || item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    resultOfSearch.add(ItemMapper.toItemDto(item));
                }
            }
        }
        return resultOfSearch;
    }

    @Override
    public void delete(Long id) {
        itemMap.remove(id);
    }
}
