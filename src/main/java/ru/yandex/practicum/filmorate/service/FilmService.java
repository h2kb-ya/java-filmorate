package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {

    Collection<Film> getFilms();

    Film getFilm(Integer id);

    Film create(Film film);

    Film update(Film film);

    void like(Integer id, Integer userId);

    void dislike(Integer id, Integer userId);

    Collection<Film> getPopular(Integer count);

    Collection<Film> getDirectorFilms(Integer id, String sortBy);
}