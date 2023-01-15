package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
public class Item {
    Long id;
    @NotBlank
    @NonNull
    @Size(max = 50)
    String name;
    @Size(max = 200)
    String description;
    boolean availableStatus;
    User owner;
    @Positive
    Long currentRequestId;
}
