package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmRepository {

    Film create(Film film);

    Film update(Film film);

    void delete(Film film);

    void deleteAll();

    Collection<Film> findAll();

    Optional<Film> findById(int id);

    Collection<Film> getPopular(int count, Integer genreId, Integer year);
}
