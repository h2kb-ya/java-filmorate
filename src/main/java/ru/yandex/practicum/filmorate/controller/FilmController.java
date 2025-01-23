package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.util.FilmorateConstants.DEFAULT_COUNT_VALUE_FOR_GETTING_POPULAR_FILMS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable @Positive Integer id) {
        return filmService.getFilm(id);
    }

    @PostMapping
    public Film create(@RequestBody @Valid final Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid final Film film) {
        return filmService.update(film);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") @Positive final Integer id) {
        filmService.deleteById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(
            @PathVariable @Positive final Integer id,
            @PathVariable final Integer userId
    ) {
        filmService.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislike(
            @PathVariable @Positive final Integer id,
            @PathVariable final Integer userId
    ) {
        filmService.dislike(id, userId);
    }

    @GetMapping("/common")
    public Collection<Film> getCommonFilms(
            @RequestParam @Positive Integer userId,
            @RequestParam @Positive Integer friendId
    ) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(
            @RequestParam(name = "count", defaultValue = DEFAULT_COUNT_VALUE_FOR_GETTING_POPULAR_FILMS) @Positive int count,
            @RequestParam(name = "genreId", required = false) Integer genreId,
            @RequestParam(name = "year", required = false) Integer year
    ) {
        return filmService.getPopular(count, genreId, year);
    }

    @GetMapping("/director/{id}")
    public Collection<Film> getDirectorFilms(@PathVariable("id") Integer id,
                                             @RequestParam String sortBy) {
        return filmService.getDirectorFilms(id, sortBy);
    }

    @GetMapping("/search")
    public Collection<Film> search(@RequestParam String query, @RequestParam String by) {
        return filmService.search(query, by);
    }
}