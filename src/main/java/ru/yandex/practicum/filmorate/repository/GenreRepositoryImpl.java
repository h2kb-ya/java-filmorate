package ru.yandex.practicum.filmorate.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mapper.GenreRowMapper;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreRepositoryImpl implements GenreRepository {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "SELECT * FROM genres ORDER BY id";

        log.info("Finding all genres");
        return jdbcTemplate.query(sqlQuery, genreRowMapper);
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";

        try {
            log.info("Finding genre by id {}",  id);
            Genre genre = jdbcTemplate.queryForObject(sqlQuery, genreRowMapper, id);
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isExists(Integer id) {
        String sqlQuery = "SELECT COUNT(*) FROM genres WHERE id = ?";

        log.info("Checking if genres with id {} exists", id);
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);

        return count != null && count > 0;
    }
}
