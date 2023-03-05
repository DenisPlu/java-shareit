package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequest create(ItemRequest itemRequest, Long requesterId) {
        try {
            if (!userRepository.getReferenceById(requesterId).equals(null)) {
                itemRequest.setRequesterId(requesterId);
                itemRequest.setCreated(LocalDateTime.now());
                return itemRequestRepository.save(itemRequest);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User с заданным id в заголовке X-Sharer-User-Id не существует");
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Не корректно задан User");
        }
    }

    @Override
    public List<ItemRequestDto> getAllByOwner(Long ownerId) {
        if (Optional.ofNullable(userRepository.getReferenceById(ownerId).getName()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя, указанного в запросе не существует в базе");
        } else {
            List<ItemRequest> itemRequestsList = itemRequestRepository.findByRequesterId(ownerId);
            List<ItemRequestDto> itemRequestsDtoList = new ArrayList<>();
            return makeItemRequestDtoList(itemRequestsDtoList, itemRequestsList);
        }
    }

    @Override
    public List<ItemRequestDto> getAll(Integer from, Long ownerId, String size) {
        List<ItemRequestDto> itemRequestsDtoList = new ArrayList<>();
        List<ItemRequest> itemRequestsList;
        if (size.equals("NoLimit")) {
            itemRequestsList = itemRequestRepository.searchFromWithoutLimit(ownerId, from);
        } else if ((from < 0) || (Integer.parseInt(size) < 0) || ((from == 0) && (Integer.parseInt(size) == 0))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное значение параметров");
        } else {
            itemRequestsList = itemRequestRepository.searchFromWithLimit(ownerId, Integer.parseInt(size), from);
        }
        return makeItemRequestDtoList(itemRequestsDtoList, itemRequestsList);
    }

    private List<ItemRequestDto> makeItemRequestDtoList(List<ItemRequestDto> itemRequestsDtoList, List<ItemRequest> itemRequestsList) {
        for (ItemRequest itemRequest : itemRequestsList) {
            List<Item> itemListByRequest = itemRepository.findByRequestId(itemRequest.getId());
            List<ItemDtoForRequest> itemDtoListByRequest = itemListByRequest.stream().map(ItemMapper::toItemDtoForRequest).collect(Collectors.toList());
            itemRequestsDtoList.add(ItemRequestMapper.toItemRequestDto(itemRequest, itemDtoListByRequest));
        }
        itemRequestsDtoList.sort(Comparator.comparing(ItemRequestDto::getCreated).reversed());
        return itemRequestsDtoList;
    }

    @Override
    public ItemRequestDto getByRequestId(Long userId, Long requestId) {
            try {
                userRepository.getReferenceById(userId).getName();
                ItemRequest itemRequest = itemRequestRepository.getReferenceById(requestId);
                List<Item> itemListByRequest = itemRepository.findByRequestId(itemRequest.getId());
                List<ItemDtoForRequest> itemDtoListByRequest = itemListByRequest.stream().map(ItemMapper::toItemDtoForRequest).collect(Collectors.toList());
                return ItemRequestMapper.toItemRequestDto(itemRequest, itemDtoListByRequest);
            } catch (EntityNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ItemRequest, указанного в запросе не существует в базе");

            }
    }
}
