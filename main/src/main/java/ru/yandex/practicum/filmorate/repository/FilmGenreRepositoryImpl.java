package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

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
    public void delete(Film film) {

    }
}
