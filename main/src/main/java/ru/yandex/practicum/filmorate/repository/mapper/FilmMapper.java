package ru.yandex.practicum.filmorate.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

@Component
public class FilmMapper {

    public Film mapFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("film_name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        long duration = rs.getLong("duration");

        int mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa_name");
        Mpa mpa = new Mpa(mpaId, mpaName);

        Film film = new Film(name, description, releaseDate, duration);
        film.setId(id);
        film.setMpa(mpa);
        film.setGenres(new HashSet<>());

        return film;
    }

    public void mapGenres(ResultSet rs, Film film) throws SQLException {
        int genreId = rs.getInt("genre_id");
        if (genreId != 0) {
            String genreName = rs.getString("genre_name");
            Genre genre = new Genre(genreId, genreName);
            film.getGenres().add(genre);
        }
    }

    public void mapLikes(ResultSet rs, Film film) throws SQLException {
        int likes = rs.getInt("likes");
        film.setLikes(likes);
    }
}
