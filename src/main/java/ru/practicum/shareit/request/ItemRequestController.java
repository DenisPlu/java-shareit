package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRequestState;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.UnsupportedStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoDescription;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    final String USER_ID = "X-Sharer-User-Id";
    private final ItemRequestServiceImpl itemRequestService;
    @PostMapping
    public ItemRequest create(@RequestBody @Valid ItemRequest itemRequest,
                              @RequestHeader(value = USER_ID) Long requesterId) {
        log.info("Received a request to create a new itemRequest: {}", itemRequest);
        return itemRequestService.create(itemRequest, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByOwner(@RequestHeader(value = USER_ID) Long ownerId) {
        log.info("Received a request to get all ItemRequests of an owner id {}", ownerId);
        return itemRequestService.getAllByOwner(ownerId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(value = USER_ID) Long ownerId,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "NoLimit") String size) {
        log.info("Received a request to get all ItemRequests from {} and size {}", from, size);
        return itemRequestService.getAll(from, ownerId, size);
    }
    @GetMapping("/{requestId}")
    public ItemRequestDto getByRequestId(@RequestHeader(value = USER_ID) Long userId,
                                         @PathVariable Long requestId) {
        log.info("Received a request to get ItemRequest {}", requestId);
        return itemRequestService.getByRequestId(userId, requestId);
    }
}
