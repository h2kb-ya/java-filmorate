package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Qualifier("userRepositoryImpl")
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Override
    public Collection<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User get(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id " + userId + " не найден."));
    }

    @Override
    public User create(final User user) {
        return userRepository.create(user);
    }

    @Override
    public User update(final User user) {
        if (userExists(user.getId())) {
            userRepository.update(user);
        }

        return user;
    }

    @Override
    public void deleteById(Integer id) {
        if (userExists(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException("Пользователь с id - " + id + "не найден.");
        }
    }

    @Override
    public void addFriend(final Integer userId, final Integer friendId) {
        if (userExists(userId) && userExists(friendId)) {
            friendshipRepository.addFriend(userId, friendId);
        }
    }

    @Override
    public void removeFriend(final Integer userId, final Integer friendId) {
        if (userExists(userId) && userExists(friendId)) {
            friendshipRepository.removeFriend(userId, friendId);
        }
    }

    @Override
    public Collection<User> getFriends(final Integer userId) {
        userExists(userId);

        return friendshipRepository.getFriends(userId).stream()
                .map(this::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<User> getCommonFriends(final Integer firstUserId, final Integer secondUserId) {
        userExists(firstUserId);
        userExists(secondUserId);
        Collection<Integer> firstUserFriends = friendshipRepository.getFriends(firstUserId);

        Collection<Integer> secondUserFriends = friendshipRepository.getFriends(secondUserId);

        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .map(this::get)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean userExists(final Integer userId) {
        User user = get(userId);

        return user != null;
    }
}
