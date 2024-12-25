package ru.yandex.practicum.filmorate.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mapper.MpaRowMapper;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaRepositoryImpl implements MpaRepository {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public List<Mpa> findAll() {
        String sqlQuery = "SELECT * FROM mpa_ratings ORDER BY id";

        log.info("Finding all ratings");
        return jdbcTemplate.query(sqlQuery, mpaRowMapper);
    }

    @Override
    public Optional<Mpa> findById(Integer id) {
        String sqlQuery = "SELECT * FROM mpa_ratings WHERE id = ?";

        try {
            log.info("Finding rating by id {}", id);
            Mpa rating = jdbcTemplate.queryForObject(sqlQuery, mpaRowMapper, id);
            return Optional.ofNullable(rating);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
}
