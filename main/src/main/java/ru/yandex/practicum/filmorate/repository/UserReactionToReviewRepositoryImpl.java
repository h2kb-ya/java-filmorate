package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.UserReactionToReview;
import ru.yandex.practicum.filmorate.repository.mapper.UserReactionToReviewRowMapper;

import java.sql.PreparedStatement;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserReactionToReviewRepositoryImpl implements UserReactionToReviewRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserReactionToReviewRowMapper userReactionToReviewRowMapper;

    private static final String INSERT_INTO_USER_REACTIONS_TO_REVIEWS = """
            INSERT INTO user_reactions_to_reviews(user_id, review_id, reaction)
            VALUES (?, ?, ?)
            """;
    private static final String SELECT_REACTION_BY_USER_ID_AND_REVIEW_ID = """
            SELECT reaction_id, user_id, review_id, reaction
            FROM user_reactions_to_reviews
            WHERE user_id = ? AND review_id = ?
            """;
    private static final String UPDATE_CURR_REACTION = """
            UPDATE user_reactions_to_reviews
            SET reaction = ?
            WHERE reaction_id = ?
            """;
    private static final String DELETE_CURR_REACTION = """
            DELETE FROM user_reactions_to_reviews
            WHERE reaction_id = ?
            """;

    @Override
    public UserReactionToReview addNewReaction(UserReactionToReview reaction) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(INSERT_INTO_USER_REACTIONS_TO_REVIEWS,
                    new String[]{"reaction_id"});
            stmt.setInt(1, reaction.getUserId());
            stmt.setInt(2, reaction.getReviewId());
            stmt.setString(3, String.valueOf(reaction.getReaction()));
            return stmt;
        };

        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        reaction.setReactionId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        final Integer reactionId = reaction.getReactionId();

        if (reactionId == null) {
            final String errorMessage = "Не удалось сохранить реакцию: " + reaction;
            log.error(errorMessage);
            throw new DataIntegrityViolationException(errorMessage);
        }

        return getReaction(reaction.getUserId(), reaction.getReviewId());
    }

    @Override
    public UserReactionToReview getReaction(Integer userId, Integer reviewId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_REACTION_BY_USER_ID_AND_REVIEW_ID,
                    userReactionToReviewRowMapper, userId, reviewId);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    @Override
    public UserReactionToReview updateReaction(UserReactionToReview reaction) {
        try {
            jdbcTemplate.update(UPDATE_CURR_REACTION,
                    reaction.getReaction(),
                    reaction.getReactionId()
            );
        } catch (DataAccessException e) {
            final String errorMessage = "Не удалось обновить реакцию: " + reaction;
            log.error(errorMessage);
            throw new DataIntegrityViolationException(errorMessage, e.getCause());
        }

        return getReaction(reaction.getUserId(), reaction.getReviewId());
    }

    @Override
    public void deleteReaction(UserReactionToReview reaction) {
        try {
            jdbcTemplate.update(DELETE_CURR_REACTION, reaction.getReactionId());
        } catch (DataAccessException e) {
            final String errorMessage = "Не удалось удалить реакцию:" + reaction;
            log.error(errorMessage);
            throw new DataIntegrityViolationException(errorMessage, e.getCause());
        }
    }
}
