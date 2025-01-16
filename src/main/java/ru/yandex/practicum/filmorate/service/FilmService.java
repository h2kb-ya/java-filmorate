package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmService {

    Collection<Film> getFilms();

    Film getFilm(Integer id);

    Film create(Film film);

    Film update(Film film);

    void deleteById(Integer id);

    void like(Integer id, Integer userId);

    void dislike(Integer id, Integer userId);

    Collection<Film> getPopular(Integer count);
}
