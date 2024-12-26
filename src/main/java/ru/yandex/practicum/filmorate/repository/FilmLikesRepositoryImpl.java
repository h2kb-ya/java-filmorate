package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmLikesRepositoryImpl implements FilmLikesRepository {

    private final JdbcTemplate jdbcTemplate;

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
}
