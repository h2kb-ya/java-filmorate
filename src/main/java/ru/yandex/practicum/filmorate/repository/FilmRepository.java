package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface FilmRepository {

    Film create(Film film);

    Film update(Film film);

    void deleteById(Integer id);

    void deleteAll();

    Collection<Film> findAll();

    Optional<Film> findById(int id);

    Collection<Film> getPopular(int count, Integer genreId, Integer year);

    Collection<Film> findFilmsByIds(Set<Integer> filmIds);

    Collection<Film> getDirectorFilms(Integer id, String sortBy);

    Collection<Film> search(String query, String by);
}