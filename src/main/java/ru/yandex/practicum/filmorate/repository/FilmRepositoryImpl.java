package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.mapper.FilmExtractor;
import ru.yandex.practicum.filmorate.repository.mapper.FilmsExtractor;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final FilmGenreRepository filmGenreRepository;
    private final FilmDirectorRepository filmDirectorRepository;
    private final FilmExtractor filmExtractor;
    private final FilmsExtractor filmsExtractor;

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_rating_id) " +
                          "VALUES (:name, :description, :release_date, :duration, :mpa_rating_id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        log.info("Creating new film: {}", film);
        namedParameterJdbcOperations.update(sqlQuery, new MapSqlParameterSource(film.toMap()), keyHolder);

        Integer id = null;
        if (keyHolder.getKey() != null) {
            id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        }

        if (id != null) {
            film.setId(id);
        } else {
            throw new DataIntegrityViolationException("He удалось сохранить фильм: " + film);
        }

        if (!film.getGenres().isEmpty()) {
            filmGenreRepository.add(film);
        }

        if (!film.getDirectors().isEmpty()) {
            filmDirectorRepository.setFilmDirectors(film.getId(), film.getDirectors());
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE films SET name = :name, description = :description, release_date = :release_date, " +
                          "duration = :duration, mpa_rating_id = :mpa_rating_id WHERE id = :id";

        filmDirectorRepository.deleteFilmDirectors(film.getId());

        Map<String, Object> params = film.toMap();
        params.put("id", film.getId());

        log.info("Updating film {}", film);
        int updatedRow = namedParameterJdbcOperations.update(sqlQuery, params);

        if (updatedRow == 0) {
            throw new DataIntegrityViolationException("He удалось обновить фильм: " + film);
        }
        filmDirectorRepository.setFilmDirectors(film.getId(), film.getDirectors());

        return film;
    }

    @Override
    public void deleteById(Integer id) {
        String sqlQuery = "DELETE FROM films WHERE id = ?";

        log.info("Deleting film with ID - {}", id);
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void deleteAll() {
        String sqlQuery = "DELETE FROM films";

        log.info("Deleting all films");
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = """
                SELECT f.id AS film_id, f.name AS film_name, f.description, f.release_date, f.duration,
                        mpa.id AS mpa_id, mpa.name AS mpa_name,
                        g.id AS genre_id, g.name AS genre_name,
                        d.id AS director_id, d.name AS director_name,
                        COUNT(fl.film_id) AS likes
                FROM films f
                JOIN mpa_ratings mpa ON f.mpa_rating_id = mpa.id
                LEFT JOIN film_genres fg ON f.id = fg.film_id
                LEFT JOIN genres g ON g.id = fg.genre_id
                LEFT JOIN film_directors fd ON f.id = fd.film_id
                LEFT JOIN directors d ON d.id = fd.director_id
                LEFT JOIN film_likes fl ON f.id = fl.film_id
                GROUP BY f.id, f.name, f.description, f.release_date, f.duration, mpa.id, mpa.name, g.id, g.name, d.id, d.name
                """;

        log.info("Finding all films");
        return jdbcTemplate.query(sqlQuery, filmsExtractor);
    }

    @Override
    public Optional<Film> findById(int id) {
        String sqlQuery = """
                SELECT f.id AS film_id, f.name AS film_name, f.description, f.release_date, f.duration,
                        mpa.id AS mpa_id, mpa.name AS mpa_name,
                        g.id AS genre_id, g.name AS genre_name,
                        d.id AS director_id, d.name AS director_name,
                        COUNT(fl.film_id) AS likes
                FROM films f
                JOIN mpa_ratings mpa ON f.mpa_rating_id = mpa.id
                LEFT JOIN film_genres fg ON f.id = fg.film_id
                LEFT JOIN genres g ON g.id = fg.genre_id
                LEFT JOIN film_directors fd ON f.id = fd.film_id
                LEFT JOIN directors d ON d.id = fd.director_id
                LEFT JOIN film_likes fl ON f.id = fl.film_id
                WHERE f.id = ?
                GROUP BY f.id, f.name, f.description, f.release_date, f.duration, mpa.id, mpa.name, g.id, g.name, d.id, d.name
                """;

        log.info("Finding film by id {}", id);
        return Optional.ofNullable(jdbcTemplate.query(sqlQuery, filmExtractor, id));
    }

    @Override
    public List<Film> getPopular(int count) {
        String sqlQuery = """
                WITH PopularFilms AS (
                    SELECT f.id AS film_id, f.name AS film_name, f.description, f.release_date, f.duration,
                            mpa.id AS mpa_id, mpa.name AS mpa_name,
                            COUNT(fl.film_id) AS likes
                    FROM films f
                            JOIN mpa_ratings mpa ON f.mpa_rating_id = mpa.id
                            LEFT JOIN film_likes fl ON f.id = fl.film_id
                    GROUP BY f.id, f.name, f.description, f.release_date, f.duration, mpa.id, mpa.name
                    ORDER BY likes DESC
                    LIMIT ?
                )
                SELECT pf.film_id, pf.film_name, pf.description, pf.release_date, pf.duration,
                        pf.mpa_id, pf.mpa_name,
                        g.id AS genre_id, g.name AS genre_name,
                        d.id AS director_id, d.name AS director_name,
                        pf.likes
                FROM PopularFilms pf
                        LEFT JOIN film_genres fg ON pf.film_id = fg.film_id
                        LEFT JOIN genres g ON g.id = fg.genre_id
                        LEFT JOIN film_directors fd ON pf.film_id = fd.film_id
                        LEFT JOIN directors d ON d.id = fd.director_id
                ORDER BY pf.likes DESC, pf.film_id, g.id;
                """;

        log.info("Getting popular films");
        return jdbcTemplate.query(sqlQuery, filmsExtractor, count);
    }

    @Override
    public Collection<Film> getDirectorFilms(Integer directorId, String sortBy) {
        String sqlQuery = getSqlQuery();

        sqlQuery = sqlQuery + "d.id = ? ORDER BY ";

        if (sortBy.equals("year")) {
            sqlQuery = sqlQuery + "sf.release_date ASC;";
        } else if (sortBy.equals("likes")) {
            sqlQuery = sqlQuery + "sf.likes DESC;";
        } else {
            throw new NotFoundException("Тип сортировки " + sortBy + " не найден!");
        }

        log.info("Getting director films");
        return jdbcTemplate.query(sqlQuery, filmsExtractor, directorId);
    }

    @Override
    public Collection<Film> search(String query, String by) {
        String sqlQuery = getSqlQuery();

        String byDirector = "LOWER(d.name) LIKE '%" + query.toLowerCase() + "%'";
        String byTitle = "LOWER(sf.film_name) LIKE '%" + query.toLowerCase() + "%'";

        sqlQuery = switch (by) {
            case "director" -> sqlQuery + byDirector;
            case "title" -> sqlQuery + byTitle;
            case "title,director", "director,title" -> sqlQuery + byDirector + " OR " + byTitle;
            default -> throw new NotFoundException("Неверный параметр поиска: " + by + "!");
        };

        sqlQuery += "ORDER BY sf.likes DESC;";

        log.info("Getting search");
        return jdbcTemplate.query(sqlQuery, filmsExtractor);
    }

    private static String getSqlQuery() {
        return """
                WITH SortedFilms AS (
                    SELECT f.id AS film_id, f.name AS film_name, f.description, f.release_date, f.duration,
                            mpa.id AS mpa_id, mpa.name AS mpa_name,
                            COUNT(fl.film_id) AS likes
                    FROM films f
                            JOIN mpa_ratings mpa ON f.mpa_rating_id = mpa.id
                            LEFT JOIN film_likes fl ON f.id = fl.film_id
                    GROUP BY f.id, f.name, f.description, f.release_date, f.duration, mpa.id, mpa.name
                    ORDER BY likes DESC
                )
                SELECT sf.film_id, sf.film_name, sf.description, sf.release_date, sf.duration,
                        sf.mpa_id, sf.mpa_name,
                        g.id AS genre_id, g.name AS genre_name,
                        d.id AS director_id, d.name AS director_name,
                        sf.likes
                FROM SortedFilms sf
                        LEFT JOIN film_genres fg ON sf.film_id = fg.film_id
                        LEFT JOIN genres g ON g.id = fg.genre_id
                        LEFT JOIN film_directors fd ON sf.film_id = fd.film_id
                        LEFT JOIN directors d ON d.id = fd.director_id
                WHERE
                """;
    }
}