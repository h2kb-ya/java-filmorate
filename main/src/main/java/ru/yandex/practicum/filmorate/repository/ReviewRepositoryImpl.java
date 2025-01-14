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
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.mapper.ReviewRowMapper;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ReviewRowMapper reviewRowMapper;

    private static final String INSERT_INTO_REVIEWS = """
            INSERT INTO reviews(content, positive, user_id, film_id, useful)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_CURR_REVIEW = """
            UPDATE reviews
            SET content = ?, positive = ?, user_id = ?, film_id = ?
            WHERE review_id = ?
            """;
    private static final String DELETE_CURR_REVIEW = """
            DELETE FROM reviews
            WHERE review_id = ?
            """;
    private static final String SELECT_REVIEW_BY_ID = """
            SELECT review_id, content, positive, user_id, film_id, useful
            FROM reviews
            WHERE review_id = ?
            """;
    private static final String SELECT_ALL_REVIEWS_WITH_LIMIT = """
            SELECT review_id, content, positive, user_id, film_id, useful
            FROM reviews
            ORDER BY useful DESC
            LIMIT ?
            """;
    private static final String SELECT_REVIEWS_BY_FILM_ID_WITH_LIMIT = """
            SELECT review_id, content, positive, user_id, film_id, useful
            FROM reviews
            WHERE film_id = ?
            ORDER BY useful DESC
            LIMIT ?
            """;

    @Override
    public Review addNewReview(Review newReview) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(INSERT_INTO_REVIEWS, new String[]{"review_id"});
            stmt.setString(1, newReview.getContent());
            stmt.setBoolean(2, newReview.getIsPositive());
            stmt.setInt(3, newReview.getUserId());
            stmt.setInt(4, newReview.getFilmId());
            stmt.setInt(5, newReview.getUseful());
            return stmt;
        };

        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        newReview.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        final Integer reviewId = newReview.getReviewId();

        if (reviewId == null) {
            final String errorMessage = "Не удалось сохранить отзыв: " + newReview;
            log.error(errorMessage);
            throw new DataIntegrityViolationException(errorMessage);
        }

        return getReviewById(reviewId);
    }

    @Override
    public Review updateCurrentReview(Review currentReview) {
        try {
            jdbcTemplate.update(UPDATE_CURR_REVIEW,
                    currentReview.getContent(),
                    currentReview.getIsPositive(),
                    currentReview.getUserId(),
                    currentReview.getFilmId(),
                    currentReview.getReviewId()
            );
        } catch (DataAccessException e) {
            final String errorMessage = "Не удалось обновить отзыв: " + currentReview;
            log.error(errorMessage);
            throw new DataIntegrityViolationException(errorMessage, e.getCause());
        }

        return getReviewById(currentReview.getReviewId());
    }

    @Override
    public void deleteCurrentReview(Integer reviewId) {
        try {
            jdbcTemplate.update(DELETE_CURR_REVIEW, reviewId);
        } catch (DataAccessException e) {
            final String errorMessage = "Не удалось удалить отзыв с id=" + reviewId;
            log.error(errorMessage);
            throw new DataIntegrityViolationException(errorMessage, e.getCause());
        }
    }

    @Override
    public Review getReviewById(Integer reviewId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_REVIEW_BY_ID, reviewRowMapper, reviewId);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    @Override
    public Collection<Review> getReviewsList(Integer count) {
        try {
            return jdbcTemplate.queryForStream(SELECT_ALL_REVIEWS_WITH_LIMIT, reviewRowMapper, count).toList();
        } catch (DataAccessException e) {
            final String errorMessage = "Не удалось получить список отзывов.";
            log.error(errorMessage);
            throw new DataIntegrityViolationException(errorMessage, e.getCause());
        }
    }

    @Override
    public Collection<Review> getReviewsListByFilmId(Integer filmId, Integer count) {
        try {
            return jdbcTemplate.queryForStream(SELECT_REVIEWS_BY_FILM_ID_WITH_LIMIT, reviewRowMapper, filmId, count)
                    .toList();
        } catch (DataAccessException e) {
            final String errorMessage = "Не удалось получить список отзывов на фильм с id=" + filmId;
            log.error(errorMessage);
            throw new DataIntegrityViolationException(errorMessage, e.getCause());
        }
    }

}
