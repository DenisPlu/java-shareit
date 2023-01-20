package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    public List<User> getAll();

    public User get(Long id);

    public User create(User user);

    public User update(Long id, User user);

    public void delete(Long id);
}
