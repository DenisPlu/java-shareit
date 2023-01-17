package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@RequiredArgsConstructor
public class UserService {
    private final UserInMemoryStorage userInMemoryStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public List<User> getAll() {
        return userInMemoryStorage.getAll();
    }

    public User getUser(Long id) {
        return userInMemoryStorage.getUser(id);
    }

    public User create (User user){
        return userInMemoryStorage.create(user);
    }

    public User update(Long id, User user) {
        return userInMemoryStorage.update(id, user);
    }

    public void delete(Long id) {
        userInMemoryStorage.delete(id);
    }
}