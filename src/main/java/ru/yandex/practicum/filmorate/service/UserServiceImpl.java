package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User create(final User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(final User user) {
        if (userExists(user.getId())) {
            userStorage.update(user);
        }

        return user;
    }

    @Override
    public void addFriend(final Integer userId, final Integer friendId) {
        User user = userStorage.getUser(userId);
        user.addFriend(friendId);

        User friend = userStorage.getUser(friendId);
        friend.addFriend(userId);

        userStorage.update(user);
        userStorage.update(friend);
    }

    @Override
    public void removeFriend(final Integer userId, final Integer friendId) {
        User user = userStorage.getUser(userId);
        user.removeFriend(friendId);

        User friend = userStorage.getUser(friendId);
        friend.removeFriend(userId);

        userStorage.update(user);
        userStorage.update(friend);
    }

    @Override
    public Collection<User> getFriends(final Integer userId) {
        return userStorage.getUser(userId).getFriends().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<User> getCommonFriends(final Integer firstUserId, final Integer secondUserId) {
        Set<Integer> firstUserFriends = userStorage.getUser(firstUserId).getFriends();

        Set<Integer> secondUserFriends = userStorage.getUser(secondUserId).getFriends();

        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .map(userStorage::getUser)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean userExists(final Integer userId) {
        User user = userStorage.getUser(userId);

        return user != null;
    }
}
