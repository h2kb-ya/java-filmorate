package ru.yandex.practicum.filmorate.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
@RequiredArgsConstructor
public class FilmExtractor implements ResultSetExtractor<Film> {

    private final FilmMapper filmMapper;

    @Override
    public Film extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) {
            return null;
        }

        Film film = filmMapper.mapFilm(rs);

        do {
            filmMapper.mapGenres(rs, film);
            filmMapper.mapLikes(rs, film);
        } while (rs.next());


        return film;
    }
}
