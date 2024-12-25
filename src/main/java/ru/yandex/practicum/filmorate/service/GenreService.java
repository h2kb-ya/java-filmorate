package ru.yandex.practicum.filmorate.service;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreService {

    List<Genre> getGenres();

    Genre getGenre(Integer id);

}
