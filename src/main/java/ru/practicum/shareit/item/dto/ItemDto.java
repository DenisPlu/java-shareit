package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class ItemDto {
//    Long id;
    @NotBlank
    @NonNull
    @Size(max = 50)
    String name;
    @Size(max = 200)
    String description;
    boolean availableStatus;
}
