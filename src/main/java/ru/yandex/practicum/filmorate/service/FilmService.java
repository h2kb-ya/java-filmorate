package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film create(final Film film) {
        return filmStorage.create(film);
    }

    public Film update(final Film film) {
        return filmStorage.update(film);
    }

    public void like(final Integer id, final Integer userId) {
        filmStorage.getFilm(id).ifPresentOrElse(
                film -> {
                    film.like(userId);
                    filmStorage.update(film);
                },
                () -> {
                    throw new NotFoundException("Фильм не найден");
                }
        );
    }

    public void dislike(Integer id, Integer userId) {
        filmStorage.getFilm(id).ifPresentOrElse(
                film -> {
                    film.dislike(userId);
                    filmStorage.update(film);
                },
                () -> {
                    throw new NotFoundException("Фильм не найден");
                }
        );
    }

    public Collection<Film> getPopular(int count) {
        if (count == 0) {
            count = 10;
        }

        return filmStorage.getPopular(count);
    }
}