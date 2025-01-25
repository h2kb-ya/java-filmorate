package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    public List<DirectorDto> getAll() {
        return directorService.getAll().stream().map(DirectorMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public DirectorDto getById(@PathVariable @Positive Integer id) {
        return DirectorMapper.toDto(directorService.getById(id));
    }

    @PostMapping
    public DirectorDto create(@RequestBody @Valid final Director director) {
        return DirectorMapper.toDto(directorService.create(director));
    }

    @PutMapping
    public DirectorDto update(@RequestBody @Valid final Director director) {
        return DirectorMapper.toDto(directorService.update(director));
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable @Positive final Integer id) {
        directorService.removeById(id);
    }
}