package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(value = USER_ID) long requesterId,
                                         @RequestBody @Valid ItemRequest itemRequest) {
        log.info("Create itemRequest {}, userId={}", itemRequest, requesterId);
        return requestClient.create(requesterId, itemRequest);
    }

    @GetMapping
    public ResponseEntity<?> getAllByOwner(@RequestHeader(value = USER_ID) long userId) {
        log.info("Get all ItemRequests of an owner id {}", userId);
        return requestClient.getAllByOwner(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(value = USER_ID) long userId,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all ItemRequests from {} and size {} of user with id {}", from, size, userId);
        return requestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getByRequestId (@RequestHeader(value = USER_ID) long userId, @PathVariable Long requestId) {
        log.info("Get all ItemRequests{}, userId={}", requestId, userId);
        return requestClient.getByRequestId(userId, requestId);
    }
}
