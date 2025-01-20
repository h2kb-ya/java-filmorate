package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
@Component
public class InMemoryFilmRepositoryImpl implements FilmRepository {

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
    public void deleteById(Integer id) {
        films.remove(id);
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Optional<Film> findById(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Collection<Film> getPopular(int count, Integer genreId, Integer year) {
        return null;
    }

    @Override
    public Collection<Film> findFilmsByIds(Set<Integer> filmIds) {
        return null;
    }

    @Override
    public void deleteAll() {
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
