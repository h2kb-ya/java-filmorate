package ru.yandex.practicum.filmorate.repository.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class DirectorExtractor implements ResultSetExtractor<Director> {

    private final DirectorMapper directorMapper;

    @Override
    public Director extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) {
            return null;
        }
        return directorMapper.mapDirector(rs);
    }
}