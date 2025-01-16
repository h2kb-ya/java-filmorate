package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {

    Film create(Film film);

    Film update(Film film);

    void delete(Film film);

    void deleteAll();

    Collection<Film> findAll();

    Optional<Film> findById(int id);

    Collection<Film> getPopular(int count);

    Collection<Film> getDirectorFilms(Integer id, String sortBy);
}