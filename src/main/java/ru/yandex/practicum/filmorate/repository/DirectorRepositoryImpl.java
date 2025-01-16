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
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.mapper.DirectorExtractor;
import ru.yandex.practicum.filmorate.repository.mapper.DirectorsExtractor;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DirectorRepositoryImpl implements DirectorRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final DirectorExtractor directorExtractor;
    private final DirectorsExtractor directorsExtractor;

    @Override
    public Director create(Director director) {
        String sqlQuery = "INSERT INTO directors (name) VALUES (:name)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        log.info("Creating director {}", director);
        namedParameterJdbcOperations.update(sqlQuery, new MapSqlParameterSource(director.toMap()), keyHolder);

        Integer id = null;
        if (keyHolder.getKey() != null) {
            id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        }

        if (id != null) {
            director.setId(id);
        } else {
            throw new DataIntegrityViolationException("Не удалось сохранить режиссера: " + director);
        }

        return director;
    }

    @Override
    public Director update(Director director) {
        String sqlQuery = "UPDATE directors SET name = :name WHERE id = :id";

        Map<String, Object> params = director.toMap();
        params.put("id", director.getId());

        log.info("Updating director {}", director);
        int updatedRow = namedParameterJdbcOperations.update(sqlQuery, params);

        if (updatedRow == 0) {
            throw new NotFoundException("Режиссер c id " + director.getId() + " не найден.");
        }

        return director;
    }

    @Override
    public Optional<Director> findById(Integer id) {
        String sqlQuery = "SELECT d.id AS director_id, d.name AS director_name FROM directors d WHERE id = ?";

        log.info("Getting director by id {}", id);
        return Optional.ofNullable(jdbcTemplate.query(sqlQuery, directorExtractor, id));
    }

    @Override
    public Collection<Director> findAll() {
        String sqlQuery = "SELECT d.id AS director_id, d.name AS director_name FROM directors d ORDER BY id ASC";

        log.info("Finding all directors");
        return jdbcTemplate.query(sqlQuery, directorsExtractor);
    }

    @Override
    public void deleteById(Integer id) {
        String sqlQuery = "DELETE FROM directors WHERE id = ?";

        log.info("Deleting director {}", id);
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void deleteAll() {
        String sqlQuery = "DELETE FROM directors";

        log.info("Deleting all directors");
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public boolean isExists(Integer id) {
        String sqlQuery = "SELECT COUNT(*) FROM directors WHERE id = ?";

        log.info("Checking if directors with id {} exists", id);
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);

        return count > 0;
    }
}