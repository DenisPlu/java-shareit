package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Getter
public class UserInMemoryStorage implements UserStorage{
    private Long currentId = 1L;
    private List<User> userList = new ArrayList<>();

    @Override
    public List<User> getAll() {
        return userList;
    }

    @Override
    public User getUser(Long id) {
        try {
            return userList.get(id.intValue() - 1);
        } catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User с данным id не существует");
        }
    }

    @Override
    public User create (User user){
        if (!user.getEmail().contains("@")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный формат email");
        } else {
            if (!ifEmailExist(user.getEmail())) {
                user.setId(currentId++);
                userList.add(user);
                return user;
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Указанный email существует");
            }
        }
    }

    @Override
    public User update (Long id, User user) {
        if (Optional.ofNullable(user.getName()).isPresent()){
            userList.get(id.intValue()-1).setName(user.getName());
        }
        if (Optional.ofNullable(user.getEmail()).isPresent()){
            if (!user.getEmail().contains("@")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный формат email");
            } else {
                if (!ifEmailExist(user.getEmail())) {
                    userList.get(id.intValue()-1).setEmail(user.getEmail());
                } else {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Указанный email существует");
                }
            }
        }
        return userList.get(id.intValue() - 1);
    }

    @Override
    public void delete (Long id) {
        userList.remove(id.intValue() - 1);
    }

    private boolean ifEmailExist(String email) {
        boolean ifExist = false;
        for (User carUser : userList) {
            if (carUser.getEmail().equals(email)) {
                ifExist = true;
                break;
            }
        }
        return ifExist;
    }
}
