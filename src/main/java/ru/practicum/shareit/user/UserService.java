package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User get(Long id) {
        try {
            userRepository.getReferenceById(id).getName();
            return userRepository.getReferenceById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User с запрошенным id не существует");
        }
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(Long id, User user) {
        User curUser = userRepository.getReferenceById(id);
        if (Optional.ofNullable(user.getName()).isPresent()) {
            curUser.setName(user.getName());
        }
        if (Optional.ofNullable(user.getEmail()).isPresent()) {
            curUser.setEmail(user.getEmail());
        }
        return userRepository.save(curUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
