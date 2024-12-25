package ru.yandex.practicum.filmorate.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaRepository mpaRepository;

    @Override
    public List<Mpa> getRatings() {
        return mpaRepository.findAll();
    }

    @Override
    public Mpa getRating(Integer id) {
        return mpaRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "MPA Рейтинг с id " + id + " не найден"));
    }
}
