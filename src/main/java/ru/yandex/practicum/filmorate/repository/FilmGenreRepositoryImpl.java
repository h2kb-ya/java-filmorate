package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mapper.GenreRowMapper;

import java.util.Collection;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmGenreRepositoryImpl implements FilmGenreRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Film film) {
        String sqlQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

        for (Genre genre : film.getGenres()) {
            log.info("Adding genre {} to film {}", genre.getId(), film.getId());
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    @Override
    public void delete(Integer id) {
        String sqlQuery = "DELETE FROM film_genres WHERE film_id = ?";

        log.info("Deleting genre {}", id);
        jdbcTemplate.update(sqlQuery, id);
    }

    public Collection<Genre> getFilmGenres(int filmId) {
        String sqlQuery = "SELECT g.id, g.name FROM film_genres fg JOIN genres g ON g.id = fg.genre_id WHERE fg.film_id = ?";

        return jdbcTemplate.query(sqlQuery, new GenreRowMapper(), filmId);
    }
}