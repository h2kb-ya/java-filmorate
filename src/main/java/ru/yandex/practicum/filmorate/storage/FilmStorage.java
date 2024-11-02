package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    void delete(Film film);

    void deleteAllFilms();

    Collection<Film> getFilms();

    Optional<Film> getFilm(int id);

    Collection<Film> getPopular(int count);
}
