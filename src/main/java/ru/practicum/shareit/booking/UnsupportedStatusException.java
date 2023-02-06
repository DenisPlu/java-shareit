package ru.practicum.shareit.booking;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UnsupportedStatusException {
    String error;
    String message;

    public UnsupportedStatusException(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
