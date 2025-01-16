package ru.yandex.practicum.filmorate.repository.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DirectorsExtractor implements ResultSetExtractor<List<Director>> {

    private final DirectorMapper directorMapper;

    @Override
    public List<Director> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Director> directorMap = new LinkedHashMap<>();

        while (rs.next()) {
            int id = rs.getInt("director_id");
            Director director = directorMap.get(id);

            if (director == null) {
                director = directorMapper.mapDirector(rs);
                directorMap.put(id, director);
            }
        }
        return new ArrayList<>(directorMap.values());
    }
}