package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.FilmsExtractor;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmLikesRepositoryImpl implements FilmLikesRepository {

    private final JdbcTemplate jdbcTemplate;
    private final FilmsExtractor filmsExtractor;

    @Override
    public void like(Film film, User user) {
        String sqlQuery = "MERGE INTO film_likes (film_id, user_id) VALUES (?, ?)";

        log.info("User {} liking film {}", user.getId(), film.getId());
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public void dislike(Film film, User user) {
        String sqlQuery = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";

        log.info("User {} disliking film {}", user.getId(), film.getId());
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public List<Integer> getCommonFilmsIds(Integer firstUserId, Integer secondUserId) {
        String sqlQuery = """
                    SELECT fl1.film_id
                    FROM film_likes fl1
                    JOIN film_likes fl2
                    ON fl1.film_id = fl2.film_id
                    WHERE fl1.user_id = ? AND fl2.user_id = ?;
                """;

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("film_id"), firstUserId, secondUserId);
    }

    @Override
    public List<Integer> getLikedFilmIds(Integer userId) {
        String sqlQuery = "SELECT film_id FROM film_likes WHERE user_id = ?";

        log.info("Getting liked films for user {}", userId);
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("film_id"), userId);
    }
}
