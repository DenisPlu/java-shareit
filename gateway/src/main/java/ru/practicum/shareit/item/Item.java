package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    Long id;

    @NonNull
    @NotBlank
    @Size(max = 50)
    String name;

    @NonNull
    @NotBlank
    @Size(max = 200)
    String description;

    @NonNull
    @NotBlank
    String available;

    Long owner;

    Long requestId;
}
