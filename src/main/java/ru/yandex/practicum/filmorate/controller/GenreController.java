package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public Collection<Genre> getGenres() {
        return genreService.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable @Positive Integer id) {
        return genreService.getGenreById(id);
    }

}
