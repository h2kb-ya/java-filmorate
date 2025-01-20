package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmDirectorRepositoryImpl implements FilmDirectorRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void setFilmDirectors(Integer id, Collection<Director> directors) {
        String sqlQuery = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)";
        if (directors != null) {
            List<Integer> directorIds = directors.stream()
                    .map(Director::getId)
                    .toList();

            jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    log.info("Adding director {} to film {}", directorIds.get(i), id);
                    preparedStatement.setInt(1, id);
                    preparedStatement.setInt(2, directorIds.get(i));
                }

                @Override
                public int getBatchSize() {
                    return directorIds.size();
                }
            });
        }
    }

    @Override
    public void deleteFilmDirectors(Integer id) {
        String sqlQuery = "DELETE FROM film_directors WHERE film_id = ?";

        log.info("Deleting film directors");
        jdbcTemplate.update(sqlQuery, id);
    }
}