package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    User create(User user);

    User update(User user);

    void deleteById(Integer id);

    void deleteAll();

    Collection<User> findAll();

    Optional<User> findById(Integer userId);
}
