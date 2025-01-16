package ru.yandex.practicum.filmorate.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mapper.FilmExtractor;
import ru.yandex.practicum.filmorate.repository.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.repository.mapper.FilmsExtractor;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepositoryImpl.class, FilmExtractor.class, FilmsExtractor.class, FilmMapper.class,
        FilmGenreRepositoryImpl.class})
public class FilmRepositoryImplIntegrationTest {

    private final FilmRepositoryImpl filmRepositoryImpl;

    private static Film getTestFilm1() {
        Film film = new Film("Inception", "A thief is given a chance to erase his criminal past.",
                LocalDate.of(2010, 7, 16), 148L);
        film.setId(1);
        film.setMpa(new Mpa(3, "PG-13"));
        film.setGenres(new HashSet<>(Set.of(
                new Genre(4, "Триллер"),
                new Genre(1, "Комедия")
        )));
        film.setLikes(4);

        return film;
    }

    private static Film getTestFilm2() {
        Film film = new Film("The Matrix", "A hacker learns the shocking truth about reality.",
                LocalDate.of(1999, 3, 31), 136L);
        film.setId(2);
        film.setMpa(new Mpa(4, "R"));
        film.setGenres(new HashSet<>(Set.of(
                new Genre(4, "Триллер")
        )));
        film.setLikes(2);

        return film;
    }

    private static Film getTestFilm3() {
        Film film = new Film("Interstellar", "Explorers travel through a wormhole to find a new home.",
                LocalDate.of(2014, 11, 7), 169L);
        film.setId(3);
        film.setMpa(new Mpa(3, "PG-13"));
        film.setGenres(new HashSet<>(Set.of(
                new Genre(5, "Документальный")
        )));
        film.setLikes(1);

        return film;
    }

    private static Film getTestFilm4() {
        Film film = new Film("The Shawshank Redemption", "Two men bond and find solace in prison.",
                LocalDate.of(1994, 9, 23), 142L);
        film.setId(4);
        film.setMpa(new Mpa(4, "R"));
        film.setGenres(new HashSet<>(Set.of(
                new Genre(2, "Драма")
        )));
        film.setLikes(1);

        return film;
    }

    @Test
    public void findAll_filmsExist_returnFilms() {
        List<Film> films = filmRepositoryImpl.findAll();
        assertThat(films)
                .isNotEmpty()
                .hasSize(4)
                .allMatch(Objects::nonNull)
                .contains(getTestFilm1(), getTestFilm2(), getTestFilm3(), getTestFilm4());
    }

    @Test
    public void findById_filmExist_returnOptionalFilm() {
        Optional<Film> film = filmRepositoryImpl.findById(getTestFilm1().getId());
        assertThat(film).isNotEmpty();
        assertThat(film.get()).isEqualTo(getTestFilm1());
    }

    @Test
    public void findById_filmDoesNotExist_returnOptionalEmpty() {
        Optional<Film> film = filmRepositoryImpl.findById(999);
        assertThat(film).isEmpty();
    }

    @Test
    public void create_happyPath_returnCreatedFilm() {
        Film film = new Film("The new King", "About new man, who want to be a king",
                LocalDate.of(1987, 9, 23), 130L);
        film.setMpa(new Mpa(4, "R"));
        film.setGenres(new HashSet<>(Set.of(
                new Genre(2, "Драма")
        )));

        Integer createdFilmId = filmRepositoryImpl.create(film).getId();
        film.setId(createdFilmId);

        assertThat(filmRepositoryImpl.findById(createdFilmId).get()).isEqualTo(film);
    }

    @Test
    public void update_happyPath_returnUpdatedFilm() {
        Film film = getTestFilm1();
        film.setName("Updated Film Name");

        filmRepositoryImpl.update(film);

        assertThat(filmRepositoryImpl.findById(film.getId()).get()).isEqualTo(film);
    }

    @Test
    public void delete_filmExists_filmDeleted() {
        Optional<Film> existedFilm = filmRepositoryImpl.findById(getTestFilm1().getId());
        assertThat(existedFilm).isNotEmpty();

        filmRepositoryImpl.delete(getTestFilm1());
        existedFilm = filmRepositoryImpl.findById(getTestFilm1().getId());
        assertThat(existedFilm).isEmpty();
    }

    @Test
    public void deleteAll_filmsExist_filmsDeleted() {
        List<Film> films = filmRepositoryImpl.findAll();
        assertThat(films.size()).isEqualTo(4);

        filmRepositoryImpl.deleteAll();
        films = filmRepositoryImpl.findAll();
        assertThat(films).isEmpty();
    }

    @Test
    public void getPopular_useCount_returnPopularLimitByCount() {
        List<Film> popularFilms = filmRepositoryImpl.getPopular(3, null, null);

        assertThat(popularFilms).isNotEmpty();
        assertThat(popularFilms.size()).isEqualTo(3);
    }
}
