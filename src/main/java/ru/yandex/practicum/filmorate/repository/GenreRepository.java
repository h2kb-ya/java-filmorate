package ru.yandex.practicum.filmorate.repository;

import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreRepository {

    List<Genre> findAll();

    Optional<Genre> findById(Integer id);

}
