package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film create(final Film film) {
        return filmStorage.create(film);
    }

    public Film update(final Film film) {
        if (filmExists(film.getId())) {
            filmStorage.update(film);
        }

        return film;
    }

    public void like(final Integer id, final Integer userId) {
        userService.userExists(userId);

        Film film = filmStorage.getFilm(id);
        film.like(userId);
        filmStorage.update(film);
    }

    public void dislike(Integer id, Integer userId) {
        userService.userExists(userId);

        Film film = filmStorage.getFilm(id);
        film.dislike(userId);
        filmStorage.update(film);
    }

    public Collection<Film> getPopular(Integer count) {
        if (count == null || count == 0) {
            count = 10;
        }

        return filmStorage.getPopular(count);
    }

    public boolean filmExists(final Integer id) {
        Film film = filmStorage.getFilm(id);

        return film != null;
    }
}
