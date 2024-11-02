package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
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
        if (!films.containsKey(film.getId())) {
            log.warn("Film not found: {}", film.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }

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
    public Optional<Film> getFilm(int id) {
        return Optional.of(films.get(id));
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return films.values()
                .stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
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