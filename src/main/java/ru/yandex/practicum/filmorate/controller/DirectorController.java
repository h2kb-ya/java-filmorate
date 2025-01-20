package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    public Collection<Director> getAll() {
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public Director getById(@PathVariable @Positive Integer id) {
        return directorService.getById(id);
    }

    @PostMapping
    public Director create(@RequestBody @Valid final Director director) {
        return directorService.create(director);
    }

    @PutMapping
    public Director update(@RequestBody @Valid final Director director) {
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable @Positive final Integer id) {
        directorService.removeById(id);
    }
}