package ru.yandex.practicum.filmorate.service;

import java.util.Collection;

import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface UserService {

    Collection<User> getUsers();

    User get(Integer userId);

    User create(User user);

    User update(User user);

    void deleteById(Integer id);

    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    Collection<User> getFriends(Integer userId);

    Collection<User> getCommonFriends(Integer firstUserId, Integer secondUserId);

    boolean userExists(Integer userId);

    Collection<EventDto> getUserFeed(Integer userId);

    Collection<Film> getRecommendations(Integer userId);
}
