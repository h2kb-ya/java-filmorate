package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.User;

public interface UserRepository {

    User create(User user);

    User update(User user);

    void delete(User user);

    void deleteById(Integer userId);

    void deleteAll();

    Collection<User> findAll();

    Optional<User> findById(Integer userId);
}
