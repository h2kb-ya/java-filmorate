package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import static ru.yandex.practicum.filmorate.util.FilmorateConstants.DEFAULT_COUNT_VALUE_FOR_GETTING_POPULAR_FILMS;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserServiceImpl userServiceImpl;

    public FilmServiceImpl(FilmStorage filmStorage, UserServiceImpl userServiceImpl) {
        this.filmStorage = filmStorage;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @Override
    public Film create(final Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(final Film film) {
        if (filmExists(film.getId())) {
            filmStorage.update(film);
        }

        return film;
    }

    @Override
    public void like(final Integer id, final Integer userId) {
        userServiceImpl.userExists(userId);

        Film film = filmStorage.getFilm(id);
        film.like(userId);
        filmStorage.update(film);
    }

    @Override
    public void dislike(Integer id, Integer userId) {
        userServiceImpl.userExists(userId);

        Film film = filmStorage.getFilm(id);
        film.dislike(userId);
        filmStorage.update(film);
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        if (count == 0) {
            count = Integer.valueOf(DEFAULT_COUNT_VALUE_FOR_GETTING_POPULAR_FILMS);
        }

        return filmStorage.getPopular(count);
    }

    @Override
    public boolean filmExists(final Integer id) {
        Film film = filmStorage.getFilm(id);

        return film != null;
    }
}
