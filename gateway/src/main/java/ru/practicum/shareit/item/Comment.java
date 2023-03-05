package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    Long id;
    @Size (max = 500)
    String text;
    Long itemId;
    Long authorId;
    LocalDateTime createdDate;
}
