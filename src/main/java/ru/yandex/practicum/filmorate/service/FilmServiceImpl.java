package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ForeignKeyViolationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.Collection;
import java.util.List;

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
    private final DirectorService directorService;

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
        if (!mpaService.isRatingExists(film.getMpa().getId())) {
            throw new ForeignKeyViolationException(
                    "Ошибка при создании фильма: указанный рейтинг с id " + film.getMpa().getId() + " не найден");
        }

        film.getGenres().stream()
                .map(Genre::getId)
                .forEach(genreId -> {
                    if (!genreService.isGenreExists(genreId)) {
                        throw new ForeignKeyViolationException(
                                "Ошибка при создании фильма: указанный жанр с id " + genreId + " не найден");
                    }
                });

        film.getDirectors().stream()
                .map(Director::getId)
                .forEach(directorId -> {
                    if (!directorService.isDirectorExists(directorId)) {
                        throw new ForeignKeyViolationException(
                                "Ошибка при создании фильма: указанный режиссер с id " + directorId + " не найден");
                    }
                });

        return filmRepository.create(film);
    }

    @Override
    public Film update(final Film film) {
        filmRepository.findById(film.getId())
                .orElseThrow(() -> new NotFoundException("Фильм c id " + film.getId() + " не найден."));
        mpaService.getRatingById(film.getMpa().getId());
        List<Integer> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .toList();

        List<Integer> directorIds = film.getDirectors().stream()
                .map(Director::getId)
                .toList();

        genreIds.forEach(genreService::getGenreById);
        directorIds.forEach(directorService::getById);

        filmRepository.update(film);

        return film;
    }

    @Override
    public void deleteById(Integer id) {
        filmRepository.findById(id).ifPresentOrElse(film -> filmRepository.deleteById(id), () -> {
            throw new NotFoundException("Фильм с id - " + id + " не найден.");
        });
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
    public Collection<Film> getPopular(int count, Integer genreId, Integer year) {
        if (count == 0) {
            count = Integer.parseInt(DEFAULT_COUNT_VALUE_FOR_GETTING_POPULAR_FILMS);
        }

        return filmRepository.getPopular(count, genreId, year);
    }

    @Override
    public Collection<Film> getDirectorFilms(Integer directorId, String sortBy) {
        return filmRepository.getDirectorFilms(directorId, sortBy);
    }
}