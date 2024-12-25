package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ForeignKeyViolationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import static ru.yandex.practicum.filmorate.util.FilmorateConstants.DEFAULT_COUNT_VALUE_FOR_GETTING_POPULAR_FILMS;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceImpl implements FilmService {

    @Qualifier("filmRepositoryImpl")
    private final FilmRepository filmRepository;
    private final UserService userService;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final FilmLikesService filmLikesService;

    @Override
    public Collection<Film> getFilms() {
        return filmRepository.findAll();
    }

    @Override
    public Film getFilm(Integer id) {
        return filmRepository.findById(id).orElseThrow(() -> new NotFoundException("Фильм c id " + id + " не найден."));
    }

    @Override
    public Film create(final Film film) {
        try {
            mpaService.getRating(film.getMpa().getId());
        } catch (NotFoundException e) {
            throw new ForeignKeyViolationException("Нарушение целостности внешних ключей: " + e.getMessage());
        }

        film.getGenres().stream()
                .map(Genre::getId)
                .forEach(genreId -> {
                    try {
                        genreService.getGenre(genreId);
                    } catch (NotFoundException e) {
                        throw new ForeignKeyViolationException(
                                "Нарушение целостности внешних ключей: " + e.getMessage());
                    }
                });

        return filmRepository.create(film);
    }

    @Override
    public Film update(final Film film) {
        filmRepository.findById(film.getId())
                .orElseThrow(() -> new NotFoundException("Фильм c id " + film.getId() + " не найден."));
        mpaService.getRating(film.getMpa().getId());
        List<Integer> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .toList();

        genreIds.forEach(genreService::getGenre);

        filmRepository.update(film);

        return film;
    }

    @Override
    public void like(final Integer id, final Integer userId) {
        User user = userService.get(userId);

        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм c id " + id + " не найден."));

        filmLikesService.like(film, user);
    }

    @Override
    public void dislike(Integer id, Integer userId) {
        User user = userService.get(userId);

        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм c id " + id + " не найден."));

        filmLikesService.dislike(film, user);
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        if (count == 0) {
            count = Integer.valueOf(DEFAULT_COUNT_VALUE_FOR_GETTING_POPULAR_FILMS);
        }

        return filmRepository.getPopular(count);
    }
}
