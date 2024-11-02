package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    User create(User user);

    User update(User user);

    void delete(User user);

    Collection<User> getUsers();

    void clearUsers();

    Optional<User> getUser(Integer userId);
}