package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum BookingRequestState {
	ALL,
	CURRENT,
	FUTURE,
	PAST,
	REJECTED,
	WAITING;

	public static Optional<BookingRequestState> from(String stringState) {
			BookingRequestState state = BookingRequestState.valueOf(stringState);
			return Optional.of(state);
	}
}
