package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreMapper {

    public static GenreDto toDto(Genre genre) {
        if (genre == null) {
            return null;
        }
        return new GenreDto(genre.getId(), genre.getName());
    }

    public static Genre toEntity(GenreDto genreDto) {
        if (genreDto == null) {
            return null;
        }
        return new Genre(genreDto.getId(), genreDto.getName());
    }
}
