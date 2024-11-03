package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(final Film film) {
        film.setId(getNextId());

        log.info("Creating new film: {}", film);
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film update(final Film film) {
        log.info("Updating film: {}", film);
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public void delete(final Film film) {
        films.remove(film.getId());
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film getFilm(int id) {
        Film film = films.get(id);

        if (film == null) {
            throw new NotFoundException("Фильм c id " + id + " не найден");
        }

        return film;
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return films.values()
                .stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}
