package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface FilmDirectorRepository {

    void setFilmDirectors(Integer id, Collection<Director> directors);

    void deleteFilmDirectors(Integer id);
}