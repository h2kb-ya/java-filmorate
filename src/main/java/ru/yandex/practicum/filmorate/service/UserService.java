package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User create(final User user) {
        return userStorage.create(user);
    }

    public User update(final User user) {
        userStorage.getUser(user.getId()).ifPresentOrElse(
                currentUser -> {
                    userStorage.update(user);
                },
                () -> {
                    throw new NotFoundException("Пользователь c id " + user.getId() + " не найден");
                }
        );

        return user;
    }

    public void addFriend(final Integer userId, final Integer friendId) {
        userStorage.getUser(userId).ifPresentOrElse(
                user -> {
                    user.addFriend(friendId);
                    userStorage.update(user);
                },
                () -> {
                    throw new NotFoundException("Пользователь не найден");
                }
        );

        userStorage.getUser(friendId).ifPresentOrElse(
                friend -> {
                    friend.addFriend(userId);
                    userStorage.update(friend);
                },
                () -> {
                    throw new NotFoundException("Пользователь не найден");
                }
        );
    }

    public void removeFriend(final Integer userId, final Integer friendId) {
        userStorage.getUser(userId).ifPresentOrElse(
                user -> {
                    user.removeFriend(friendId);
                    userStorage.update(user);
                },
                () -> {
                    throw new NotFoundException("Пользователь не найден");
                }
        );

        userStorage.getUser(friendId).ifPresentOrElse(
                friend -> {
                    friend.removeFriend(userId);
                    userStorage.update(friend);
                },
                () -> {
                    throw new NotFoundException("Пользователь не найден");
                }
        );
    }

    public Collection<Integer> gerFriends(final Integer userId) {
        return userStorage.getUser(userId).orElseThrow(() -> new RuntimeException("TODO")).getFriends();
    }

    public Collection<Integer> getCommonFriends(final Integer firstUserId, final Integer secondUserId) {
        Set<Integer> firstUserFriends = userStorage.getUser(firstUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")).getFriends();

        Set<Integer> secondUserFriends = userStorage.getUser(secondUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")).getFriends();

        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .collect(Collectors.toSet());
    }
}
