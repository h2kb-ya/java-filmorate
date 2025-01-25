package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;

public class DirectorMapper {

    public static DirectorDto toDto(Director director) {
        if (director == null) {
            return null;
        }
        return new DirectorDto(director.getId(), director.getName());
    }

    public static Director toEntity(DirectorDto directorDto) {
        if (directorDto == null) {
            return null;
        }
        return new Director(directorDto.getId(), directorDto.getName());
    }
}
