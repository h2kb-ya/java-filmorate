package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DirectorMapper {

    public Director mapDirector(ResultSet rs) throws SQLException {
        int id = rs.getInt("director_id");
        String name = rs.getString("director_name");

        Director director = new Director(name);
        director.setId(id);

        return director;
    }
}