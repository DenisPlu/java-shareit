package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoDescription;

import java.util.List;

public interface ItemRequestService {
    ItemRequest create(ItemRequest itemRequest, Long requesterId);

    List<ItemRequestDto> getAllByOwner(Long ownerId);

    List<ItemRequestDto> getAll(Integer from, Long ownerId, String size);

    ItemRequestDto getByRequestId(Long ownerId, Long requestId);
}
