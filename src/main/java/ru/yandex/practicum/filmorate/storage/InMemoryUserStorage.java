package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User create(final User user) {
        user.setId(getNextId());

        log.info("Creating new user: {}", user);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User update(final User user) {
        log.info("Updating user: {}", user);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public void delete(final User user) {

    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public void clearUsers() {
        users.clear();
    }

    @Override
    public Optional<User> getUser(Integer userId) {
        return Optional.ofNullable(users.get(userId));
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
