package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface FilmLikesRepository {

    void like(Film film, User user);

    void dislike(Film film, User user);
}
