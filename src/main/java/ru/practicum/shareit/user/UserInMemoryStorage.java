package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Component
@RequiredArgsConstructor
@Getter
public class UserInMemoryStorage implements UserStorage {
    private Long currentId = 1L;
    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User get(Long id) {
        try {
            return userMap.get(id);
        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User с данным id не существует");
        }
    }

    @Override
    public User create(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный формат email");
        } else {
            if (!ifEmailExist(user.getEmail())) {
                user.setId(currentId);
                userMap.put(currentId++, user);
                return user;
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Указанный email существует");
            }
        }
    }

    @Override
    public User update(Long id, User user) {
        if (Optional.ofNullable(user.getName()).isPresent()) {
            userMap.get(id).setName(user.getName());
        }
        if (Optional.ofNullable(user.getEmail()).isPresent()) {
            if (!user.getEmail().contains("@")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный формат email");
            } else {
                if (!ifEmailExist(user.getEmail())) {
                    userMap.get(id).setEmail(user.getEmail());
                } else {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Указанный email существует");
                }
            }
        }
        return userMap.get(id);
    }

    @Override
    public void delete(Long id) {
        userMap.remove(id);
    }

    private boolean ifEmailExist(String email) {
        boolean ifExist = false;
        for (User carUser : userMap.values()) {
            if (carUser.getEmail().equals(email)) {
                ifExist = true;
                break;
            }
        }
        return ifExist;
    }
}
