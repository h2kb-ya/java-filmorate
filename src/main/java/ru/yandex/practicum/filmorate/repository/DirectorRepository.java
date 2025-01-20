package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Optional;

public interface DirectorRepository {

    Director create(Director director);

    Director update(Director director);

    Optional<Director> findById(Integer id);

    Collection<Director> findAll();

    void deleteById(Integer id);

    void deleteAll();

    boolean isExists(Integer id);
}