package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        user.setId(getNextId());

        log.info("Creating new user: {}", user);
        users.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        log.info("Updating user: {}", user);
        users.put(user.getId(), user);

        return user;
    }

    public void clearUsers() {
        users.clear();
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}
