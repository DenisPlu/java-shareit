package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoForUpdate {
    Long id;
    @Size(max = 50)
    String name;
    @Size(max = 200)
    String description;
    String available;
    Long requestId;
}
