package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> getRatings() {
        return mpaService.getRatings();
    }

    @GetMapping("/{id}")
    public Mpa getRating(@PathVariable @Positive Integer id) {
        return mpaService.getRatingById(id);
    }
}
