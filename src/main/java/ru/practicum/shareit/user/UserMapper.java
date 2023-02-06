package ru.practicum.shareit.user;

public class UserMapper {

    public static UserDtoId toUserDtoId(User user) {
        return new UserDtoId(
                user.getId()
        );
    }
}
