package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    // Создайте класс UserController и методы в нём для основных CRUD-операций

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return Optional.ofNullable(userService.getUser(id));
    }

    @PostMapping
    public Optional<User> create(@RequestBody @Valid User user){
        return Optional.ofNullable(userService.create(user));
    }

    @PatchMapping("/{id}")
    public Optional<User> update(@PathVariable Long id, @RequestBody User user) {
        return Optional.ofNullable(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public void delete (@PathVariable Long id) {
        userService.delete(id);
    }
}
