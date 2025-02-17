package ru.yandex.practicum.filmorate.repository;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FilmLikesRepository {

    void like(Film film, User user);

    void dislike(Film film, User user);

    Collection<Integer> getCommonFilmsIds(Integer firstUserId, Integer secondUserId);

    List<Integer> getLikedFilmIds(Integer userId);
}
