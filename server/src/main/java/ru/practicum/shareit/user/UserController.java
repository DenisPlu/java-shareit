package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        log.info("Received a request to get all users");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        log.info("Received a request to get a user with id {}", id);
        return userService.get(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Received a request to create a new user: {}", user);
        return userService.create(user);
    }

    @PatchMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        log.info("Received a request to update a user with id: {}, user: {}", id, user);
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Received a request to delete a user with id: {}", id);
        userService.delete(id);
    }
}
