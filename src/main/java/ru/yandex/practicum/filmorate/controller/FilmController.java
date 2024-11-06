package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;

import static ru.yandex.practicum.filmorate.util.FilmorateConstants.DEFAULT_COUNT_VALUE_FOR_GETTING_POPULAR_FILMS;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmServiceImpl filmServiceImpl;

    @Autowired
    public FilmController(FilmServiceImpl filmServiceImpl) {
        this.filmServiceImpl = filmServiceImpl;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmServiceImpl.getFilms();
    }

    @PostMapping
    public Film create(@RequestBody @Valid final Film film) {
        return filmServiceImpl.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid final Film film) {
        return filmServiceImpl.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(
            @PathVariable @Positive final Integer id,
            @PathVariable @Positive final Integer userId
    ) {
        filmServiceImpl.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislike(
            @PathVariable @Positive final Integer id,
            @PathVariable @Positive final Integer userId
    ) {
        filmServiceImpl.dislike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(
            @RequestParam(required = false, defaultValue = DEFAULT_COUNT_VALUE_FOR_GETTING_POPULAR_FILMS) @Positive Integer count
    ) {
        return filmServiceImpl.getPopular(count);
    }
}
