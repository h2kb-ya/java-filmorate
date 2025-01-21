package ru.yandex.practicum.filmorate.repository.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FilmsExtractor implements ResultSetExtractor<List<Film>> {

    private final FilmMapper filmMapper;

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Film> filmMap = new LinkedHashMap<>();

        while (rs.next()) {
            int id = rs.getInt("film_id");
            Film film = filmMap.get(id);

            if (film == null) {
                film = filmMapper.mapFilm(rs);
                filmMap.put(id, film);
            }

            filmMapper.mapGenres(rs, film);
            filmMapper.mapDirectors(rs, film);
            filmMapper.mapLikes(rs, film);
        }

        return new ArrayList<>(filmMap.values());
    }
}