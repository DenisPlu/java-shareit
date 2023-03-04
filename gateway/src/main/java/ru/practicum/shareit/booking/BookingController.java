package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingRequestState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;
	private static final String USER_ID = "X-Sharer-User-Id";

	@GetMapping
	public ResponseEntity<?> getAllByUser(@RequestHeader(value = USER_ID) long userId,
										  @RequestParam(name = "state", defaultValue = "all") String stateParam,
										  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
										  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		try{
			BookingRequestState state = BookingRequestState.from(stateParam).get();
			log.info("Get bookings with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
			return bookingClient.getAllByUser(userId, state, from, size);
		} catch (Exception e) {
			return new ResponseEntity<>(new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllByOwner(@RequestHeader(value = USER_ID) long userId,
											   @RequestParam(name = "state", defaultValue = "all") String stateParam,
											   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
											   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		try{
			BookingRequestState state = BookingRequestState.from(stateParam).get();
			log.info("Get bookings with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
			return bookingClient.getAllByOwner(userId, state, from, size);
		} catch (Exception e) {
			return new ResponseEntity<>(new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> get(@RequestHeader(value = USER_ID) long userId, @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PostMapping
	public ResponseEntity<Object> create(@RequestHeader(value = USER_ID) long userId,
			@RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.create(userId, requestDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> update(@PathVariable Long bookingId,
										 @RequestHeader(value = USER_ID) long userId,
										 @RequestParam String approved) {
		log.info("Update Booking with id: {}, ownerId: {}, status: {}", bookingId, userId, approved);
		return bookingClient.update(bookingId, userId, approved);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		log.info("Delete Booking with id: {}", id);
		bookingClient.delete(id);
	}
}
