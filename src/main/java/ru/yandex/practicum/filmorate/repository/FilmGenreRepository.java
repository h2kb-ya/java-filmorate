package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmGenreRepository {

    void add(Film film);

    void delete(Integer id);

}