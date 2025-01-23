package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface FilmGenreRepository {

    void add(Film film);

    void delete(Integer id);

    Collection<Genre> getFilmGenres(int filmId);
}