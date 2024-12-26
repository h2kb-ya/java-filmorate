package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FriendshipRepositoryImpl implements FriendshipRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Integer> getFriends(Integer userId) {
        String sqlQuery = "SELECT friend_id FROM friendships WHERE user_id = ?";

        log.info("Getting user friends: {}", userId);
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("friend_id"), userId);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sqlQuery = "INSERT INTO friendships (user_id, friend_id, status) VALUES (?, ?, ?)";

        log.info("Adding new friend: {} to {}", userId, friendId);
        jdbcTemplate.update(sqlQuery, userId, friendId, FriendshipStatus.PENDING.toString());
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        String sqlQuery = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";

        log.info("Deleting friend: {} from {}", userId, friendId);
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
