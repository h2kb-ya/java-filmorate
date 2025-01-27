package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.UserReactionToReview;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserReactionToReviewRowMapper implements RowMapper<UserReactionToReview> {

    @Override
    public UserReactionToReview mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        return UserReactionToReview.builder()
                .reactionId(resultSet.getInt("reaction_id"))
                .userId(resultSet.getInt("user_id"))
                .reviewId(resultSet.getInt("review_id"))
                .reaction(resultSet.getString("reaction"))
                .build();

    }

}
