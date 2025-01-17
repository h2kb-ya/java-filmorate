package ru.yandex.practicum.filmorate.repository;

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
public class InMemoryUserRepositoryImpl implements UserRepository {

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
    public void deleteById(Integer id) {

    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> findById(Integer userId) {
        User user = users.get(userId);

        if (user == null) {
            throw new NotFoundException("Пользователь c id " + userId + " не найден");
        }

        return Optional.of(user);
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
