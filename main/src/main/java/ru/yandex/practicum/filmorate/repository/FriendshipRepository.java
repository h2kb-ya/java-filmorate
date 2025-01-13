package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;

public interface FriendshipRepository {

    Collection<Integer> getFriends(Integer userId);

    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);
}
