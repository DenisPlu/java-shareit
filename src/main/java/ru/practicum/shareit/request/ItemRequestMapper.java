package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoDescription;

import java.time.LocalDateTime;
import java.util.List;

public final class ItemRequestMapper {

    private ItemRequestMapper(){};
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDtoForRequest> items) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequesterId(),
                itemRequest.getCreated(),
                items
                );
    }
    public static ItemRequest toItemRequestFromItemRequestDtoDescription(ItemRequestDtoDescription itemRequestDtoDescription, Long requesterId) {
        return new ItemRequest(
                itemRequestDtoDescription.getId(),
                itemRequestDtoDescription.getDescription(),
                requesterId,
                LocalDateTime.now()
        );
    }
}
