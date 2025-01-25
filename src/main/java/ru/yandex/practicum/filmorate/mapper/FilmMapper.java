package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {

    public static FilmDto toDto(Film film) {
        if (film == null) {
            return null;
        }

        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getLikes(),
                film.getGenres(),
                film.getDirectors(),
                film.getMpa()
        );
    }

    public static Film toEntity(FilmDto filmDto) {
        if (filmDto == null) {
            return null;
        }

        Film film = new Film(
                filmDto.getName(),
                filmDto.getDescription(),
                filmDto.getReleaseDate(),
                filmDto.getDuration()
        );
        film.setId(filmDto.getId());
        film.setLikes(filmDto.getLikes());
        film.setGenres(filmDto.getGenres());
        film.setDirectors(filmDto.getDirectors());
        film.setMpa(filmDto.getMpa());

        return film;
    }
}
