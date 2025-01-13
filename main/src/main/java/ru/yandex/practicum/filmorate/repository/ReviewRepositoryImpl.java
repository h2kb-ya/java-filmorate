package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.mapper.ReviewRowMapper;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.util.FilmorateConstants.DEFAULT_REVIEW_USEFUL_RATE;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ReviewRowMapper reviewRowMapper;

    @Override
    public Review addNewReview(Review newReview) {
        final String sqlQuery = "INSERT INTO reviews(content, positive, user_id, film_id, useful) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"review_id"});
            stmt.setString(1, newReview.getContent());
            stmt.setBoolean(2, newReview.getIsPositive());
            stmt.setInt(3, newReview.getUserId());
            stmt.setInt(4, newReview.getFilmId());
            stmt.setInt(5, DEFAULT_REVIEW_USEFUL_RATE);
            return stmt;
        };

        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        newReview.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        if (newReview.getReviewId() == null) {
            throw new DataIntegrityViolationException("Не удалось сохранить отзыв: " + newReview);
        }

        log.info("Сохранен отзыв: {}", newReview);
        return newReview;
    }

    @Override
    public Review updateCurrentReview(Review currentReview) {
        final String sqlQuery = "UPDATE reviews " +
                "SET content = ?, positive = ?, user_id = ?, film_id = ? " +
                "WHERE review_id = ?";

        jdbcTemplate.update(sqlQuery,
                currentReview.getContent(),
                currentReview.getIsPositive(),
                currentReview.getUserId(),
                currentReview.getFilmId(),
                currentReview.getReviewId()
        );

        final Review updatedReview = getReviewById(currentReview.getReviewId());
        if (updatedReview == null) {
            throw new DataIntegrityViolationException("Не удалось обновить отзыв: " + currentReview);
        }

        log.info("Обновлен отзыв: {}", updatedReview);
        return updatedReview;
    }

    @Override
    public Review deleteCurrentReview(Integer reviewId) {
        final Review deletedReview = getReviewById(reviewId);
        final String sqlQuery = "DELETE FROM reviews " +
                "WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery, reviewId);

        if (getReviewById(reviewId) != null) {
            throw new DataIntegrityViolationException("Не удалось удалить отзыв: " + deletedReview);
        }

        log.info("Удален отзыв: {}", deletedReview);
        return deletedReview;
    }

    @Override
    public Review getReviewById(Integer reviewId) {
        final String sqlQuery = "SELECT review_id, content, positive, user_id, film_id, useful " +
                "FROM reviews " +
                "WHERE review_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, reviewRowMapper, reviewId);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    @Override
    public Collection<Review> getReviewsList(Integer count) {
        final String sqlQuery = "SELECT review_id, content, positive, user_id, film_id, useful " +
                "FROM reviews " +
                "ORDER BY useful DESC " +
                "LIMIT ?";
        return jdbcTemplate.queryForStream(sqlQuery, reviewRowMapper, count).toList();
    }

    @Override
    public Collection<Review> getReviewsListByFilmId(Integer filmId, Integer count) {
        final String sqlQuery = "SELECT review_id, content, positive, user_id, film_id, useful " +
                "FROM reviews " +
                "WHERE film_id = ? " +
                "ORDER BY useful DESC " +
                "LIMIT ?";
        return jdbcTemplate.queryForStream(sqlQuery, reviewRowMapper, filmId, count).toList();
    }

    @Override
    public Review putLikeToReview(Integer reviewId) {
        final String sqlQuery = "UPDATE reviews SET useful = useful + 1 WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery, reviewId);
        return getReviewById(reviewId);
    }

    @Override
    public Review putDislikeToReview(Integer reviewId) {
        final String sqlQuery = "UPDATE reviews SET useful = useful - 1 WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery, reviewId);
        return getReviewById(reviewId);
    }

    @Override
    public Review deleteLikeFromReview(Integer reviewId) {
        final String sqlQuery = "UPDATE reviews SET useful = useful - 1 WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery, reviewId);
        return getReviewById(reviewId);
    }

    @Override
    public Review deleteDislikeFromReview(Integer reviewId) {
        final String sqlQuery = "UPDATE reviews SET useful = useful - 1 WHERE review_id = ?";
        jdbcTemplate.update(sqlQuery, reviewId);
        return getReviewById(reviewId);
    }

}
