package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String USER_ID = "X-Sharer-User-Id";
    private final BookingServiceImpl bookingService;

    @GetMapping
    public List<BookingDto> getAllByUser(@RequestParam BookingRequestState state,
                                         @RequestParam Integer from,
                                         @RequestParam Integer size,
                                         @RequestHeader(value = USER_ID) Long userId) {
        System.out.println(state);
        log.info("Received a request to get all Bookings of user with id {} and state of booking {} from {} number of {}", userId, state, from, size);
        return bookingService.getAllByUser(state, userId, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getAllByOwner(@RequestParam BookingRequestState state,
                                           @RequestParam Integer from,
                                           @RequestParam Integer size,
                                           @RequestHeader(value = USER_ID) Long ownerId) {
        log.info("Received a request to get all Bookings of an owner id {} and state of booking {}", ownerId, state);
        return new ResponseEntity<>(bookingService.getAllByOwner(state, ownerId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public BookingDto get(@PathVariable Long id, @RequestHeader(value = USER_ID) Long userId) {
        log.info("Received a request to get an Booking with id {} from user {}", id, userId);
        try {
            return bookingService.get(id, userId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking с данным id не существует");
        }
    }

    @PostMapping
    public BookingDto create(@RequestBody Booking booking, @RequestHeader(value = USER_ID) Long bookerId) {
        log.info("Received a request to create a new Booking: {}", booking);
        return bookingService.create(booking, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId, @RequestParam String approved, @RequestHeader(value = USER_ID) Long ownerId) {
        log.info("Received a request to update an Booking with id: {}, ownerId: {}, status: {}", bookingId, ownerId, approved);
        return bookingService.update(bookingId, ownerId, approved);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Received a request to delete a Booking with id: {}", id);
        bookingService.delete(id);
    }
}
