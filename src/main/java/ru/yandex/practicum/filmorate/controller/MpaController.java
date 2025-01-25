package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public Collection<MpaDto> getRatings() {
        return mpaService.getRatings().stream().map(MpaMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public MpaDto getRating(@PathVariable @Positive Integer id) {
        return MpaMapper.toDto(mpaService.getRatingById(id));
    }
}
