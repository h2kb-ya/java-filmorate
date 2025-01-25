package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.service.GenreService;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public Collection<GenreDto> getGenres() {
        return genreService.getGenres().stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GenreDto getGenre(@PathVariable @Positive Integer id) {
        return GenreMapper.toDto(genreService.getGenreById(id));
    }
}
