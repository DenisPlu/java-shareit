package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UserMapper {

    public static UserDtoId toUserDtoId(User user) {
        return new UserDtoId(
                user.getId()
        );
    }
}
