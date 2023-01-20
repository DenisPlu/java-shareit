package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserInMemoryStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemInMemoryStorage itemInMemoryStorage;
    private final UserInMemoryStorage userInMemoryStorage;

    @Override
    public List<ItemDto> getAllByOwner(Long ownerId) {
        return itemInMemoryStorage.getAllByOwner(ownerId);
    }

    @Override
    public Item get(Long id) {
        return itemInMemoryStorage.get(id);
    }

    @Override
    public List <ItemDto> searchInNameAndDescription(String text) {
        return itemInMemoryStorage.searchInNameAndDescription(text);
    }

    @Override
    public ItemDto create (ItemDto item, Long ownerId) {
        if (item.isAvailable() && Optional.ofNullable(item.getDescription()).isPresent()) {
            if (userInMemoryStorage.getUserMap().containsKey(ownerId) && item.isAvailable()) {
                return itemInMemoryStorage.create(item, ownerId);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User с заданным id в заголовке X-Sharer-User-Id не существует");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Не задана Available - доступность или описание item");
        }
    }

    @Override
    public ItemDto update(Long id, Item item, Long ownerId) {
        return itemInMemoryStorage.update(id, item, ownerId);
    }

    @Override
    public void delete(Long id) {
        itemInMemoryStorage.delete(id);
    }
}
