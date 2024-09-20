package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film film) {
        film.setId(getNextId());

        log.info("Adding new film: {}", film);
        films.put(film.getId(), film);

        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("Updating film: {}", film);

        if (!films.containsKey(film.getId())) {
            log.warn("Film not found: {}", film.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }

        films.put(film.getId(), film);

        return film;
    }

    public void clearFilms() {
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
