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
		for (BookingRequestState state : values()) {
			if (state.name().equalsIgnoreCase(stringState)) {
				return Optional.of(state);
			}
		}
		return Optional.empty();
	}
}
