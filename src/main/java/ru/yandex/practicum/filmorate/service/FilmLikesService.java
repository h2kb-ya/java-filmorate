package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface FilmLikesService {

    void like(Film film, User user);

    void dislike(Film film, User user);
}
