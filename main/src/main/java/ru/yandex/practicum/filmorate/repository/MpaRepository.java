package ru.yandex.practicum.filmorate.repository;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Mpa;

public interface MpaRepository {

    List<Mpa> findAll();

    Optional<Mpa> findById(Integer id);

    boolean isExists(Integer id);
}
