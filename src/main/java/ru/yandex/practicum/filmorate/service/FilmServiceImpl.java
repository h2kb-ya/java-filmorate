package ru.yandex.practicum.filmorate.service;

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

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        genreIds.forEach(genreService::getGenreById);

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

    public Collection<Film> getCommonFilms(Integer firstUserId, Integer secondUserId) {
        Set<Integer> commonFilmsIds = filmLikesService.getCommonFilms(firstUserId, secondUserId).stream().collect(Collectors.toSet());

        Collection<Film> getAllFilms = filmRepository.findAll();

        if (commonFilmsIds.isEmpty()) {
            return List.of();
        }

        return getAllFilms.stream()
                .filter(film -> commonFilmsIds.contains(film.getId()))
                .sorted((film1, film2) -> Integer.compare(film2.getLikes(), film1.getLikes()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Film> getPopular(Integer count) {
        if (count == 0) {
            count = Integer.valueOf(DEFAULT_COUNT_VALUE_FOR_GETTING_POPULAR_FILMS);
        }

        return filmRepository.getPopular(count);
    }
}
