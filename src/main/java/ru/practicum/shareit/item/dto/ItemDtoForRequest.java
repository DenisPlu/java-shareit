package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoForRequest {
    Long id;
    @NotBlank
    @NonNull
    @Size(max = 50)
    String name;
    String description;
    boolean available;
    Long requestId;
}
