package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public final class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequestDto itemRequestDto) {
        return new ItemRequestDto(
                itemRequestDto.getDescription(),
                itemRequestDto.getRequester(),
                itemRequestDto.getCreated()
        );
    }
}
