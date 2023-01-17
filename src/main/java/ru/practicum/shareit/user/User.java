package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User {
    Long id;
    @NonNull
    @NotBlank
    String name;
    @NonNull
    @NotBlank
    String email;
}
