package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    User create(User user);

    User update(User user);

    void delete(User user);

    void deleteAllUsers();

    Collection<User> getUsers();

    void clearUsers();

    User getUser(Integer userId);
}
