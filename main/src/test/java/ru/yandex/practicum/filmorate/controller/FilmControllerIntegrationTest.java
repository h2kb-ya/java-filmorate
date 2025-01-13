package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class FilmControllerIntegrationTest extends AbstractApplicationMvcIntegrationTest {

    @Autowired
    private FilmController filmController;

    @Autowired
    @Qualifier("filmRepositoryImpl")
    private FilmRepository filmRepository;

    @Autowired
    @Qualifier("userRepositoryImpl")
    private UserRepository userRepository;

    @Autowired
    UserServiceImpl userServiceImpl;

    @BeforeEach
    public void setUp() {
        filmRepository.deleteAll();
        userRepository.deleteAll();
    }

    private static Film getTestFilm1() {
        Film film = new Film("Inception", "A thief is given a chance to erase his criminal past.",
                LocalDate.of(2010, 7, 16), 148L);
        film.setId(1);
        film.setMpa(new Mpa(3, "PG-13"));
        film.setGenres(new HashSet<>(Set.of(
                new Genre(4, "Триллер"),
                new Genre(1, "Комедия")
        )));

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

    private static User getTestUser1() {
        return new User("TestUser1@example.com", "TestUser1", "Test User One", LocalDate.of(1990, 5, 15));
    }

    @Test
    @SneakyThrows
    void whenGetFilmsFilmsAreNotExistThenReturnEmptyList() {
        MvcResult result = mockMvc.perform(get("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Film> films = deserializeList(result, Film.class);
        assertTrue(films.isEmpty());
    }

    @Test
    @SneakyThrows
    void whenGetFilmsFilmsAreExistThenReturnFilmsList() {
        assertTrue(filmController.getFilms().isEmpty());

        Film film1 = getTestFilm1();

        Film film2 = getTestFilm2();

        filmController.create(film1);
        filmController.create(film2);

        MvcResult result = mockMvc.perform(get("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Film> films = deserializeList(result, Film.class);
        assertFalse(films.isEmpty());
        assertEquals(2, films.size());
    }

    @Test
    @SneakyThrows
    void whenAddFilmThenReturnFilm() {
        assertTrue(filmController.getFilms().isEmpty());

        Film film = getTestFilm1();

        String content = serialize(film);

        MvcResult result = mockMvc.perform(post("/films")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Film filmFromResponse = deserialize(result, Film.class);
        assertEquals(film.getName(), filmFromResponse.getName());
        assertEquals(film.getDescription(), filmFromResponse.getDescription());
        assertEquals(1, filmController.getFilms().size());
    }

    @Test
    @SneakyThrows
    void whenAddFilmNameIsEmptyThenReturn400() {
        Film film = new Film("", "Film description", LocalDate.of(2024, 6, 26), 3600L);

        String content = serialize(film);

        MvcResult result = mockMvc.perform(post("/films")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains("Name of the film must not be blank"));
    }

    @Test
    @SneakyThrows
    void whenAddFilmDescriptionIsTooBigThenReturn400() {
        Film film = new Film("Film", "Film description".repeat(20), LocalDate.of(2024, 6, 26), 3600L);

        String content = serialize(film);

        MvcResult result = mockMvc.perform(post("/films")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains("Max length of description - 200 characters"));
    }

    @Test
    @SneakyThrows
    void whenAddFilmWrongReleaseDateThenReturn400() {
        Film film = new Film("Film", "Film description", LocalDate.of(1800, 6, 26), 3600L);

        String content = serialize(film);

        MvcResult result = mockMvc.perform(post("/films")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains("Film release date must be after 1895"));
    }

    @Test
    @SneakyThrows
    void whenAddFilmDurationIsNegativeThenReturn400() {
        Film film = new Film("Film", "Film description", LocalDate.of(2024, 6, 26), -3600L);

        String content = serialize(film);

        MvcResult result = mockMvc.perform(post("/films")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains("Duration of film must be positive"));
    }

    @Test
    @SneakyThrows
    void addLike_filmExists_returnFilmWithLike() {
        assertTrue(filmController.getFilms().isEmpty());

        User user = new User("user@yandex.ru", "user", "User", LocalDate.of(1985, 1, 1));
        User savedUser = userServiceImpl.create(user);
        Film film = getTestFilm1();
        Film savedFilm = filmController.create(film);

        assertThat(savedFilm.getLikes()).isEqualTo(0);

        mockMvc.perform(put("/films/{id}/like/{userId}", savedFilm.getId(), savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(filmRepository.findById(savedFilm.getId()).get().getLikes()).isEqualTo(1);
    }

    @Test
    @SneakyThrows
    void addLike_filmDoesNotExist_returnNotFound() {
        assertTrue(filmController.getFilms().isEmpty());

        User user = new User("user@yandex.ru", "user", "User", LocalDate.of(1985, 1, 1));
        User savedUser = userServiceImpl.create(user);
        int badFilmId = 999;

        mockMvc.perform(put("/films/{id}/like/{userId}", badFilmId, savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Объект не найден"))
                .andExpect(jsonPath("$.description").value("Фильм c id " + badFilmId + " не найден."));
    }

    @Test
    @SneakyThrows
    void addLike_userDoesNotExist_returnNotFound() {
        assertTrue(filmController.getFilms().isEmpty());

        int badUserId = 999;
        Film film = getTestFilm1();
        Film savedFilm = filmController.create(film);

        mockMvc.perform(put("/films/{id}/like/{userId}", savedFilm.getId(), badUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Объект не найден"))
                .andExpect(jsonPath("$.description").value("Пользователь c id " + badUserId + " не найден."));
    }

    @Test
    @SneakyThrows
    void removeLike_filmExists_returnEmptyLikes() {
        assertTrue(filmController.getFilms().isEmpty());

        User user = new User("user@yandex.ru", "user", "User", LocalDate.of(1985, 1, 1));
        User savedUser = userServiceImpl.create(user);
        Film film = getTestFilm1();
        Film savedFilm = filmController.create(film);

        assertThat(savedFilm.getLikes()).isEqualTo(0);

        mockMvc.perform(put("/films/{id}/like/{userId}", savedFilm.getId(), savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(filmRepository.findById(savedFilm.getId()).get().getLikes()).isEqualTo(1);

        mockMvc.perform(delete("/films/{id}/like/{userId}", savedFilm.getId(), savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(filmRepository.findById(savedFilm.getId()).get().getLikes()).isEqualTo(0);
    }

    @Test
    @SneakyThrows
    void getPopular_happyPath_returnRightCountOfPopularFilms() {
        assertTrue(filmController.getFilms().isEmpty());

        IntStream.range(0, 20).forEach(i -> {
            User user = getTestUser1();
            user.setLogin("TestUser" + i);
            user.setEmail("TestUser" + i + "@example.com");
            userServiceImpl.create(user);
        });

        IntStream.range(0, 20).forEach(i -> {
            Film film = getTestFilm1();
            filmController.create(film);
        });

        List<Integer> userIds = userServiceImpl.getUsers().stream()
                .map(User::getId)
                .toList();

        List<Integer> filmIds = filmController.getFilms().stream()
                .map(Film::getId)
                .toList();

        IntStream.range(20, 100)
                .forEach(i -> filmController.like(getRandomElement(filmIds), getRandomElement(userIds)));

        MvcResult resultWithDefaultCount = mockMvc.perform(get("/films/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Film> filmsWithDefaultCount = deserializeList(resultWithDefaultCount, Film.class);
        assertEquals(10, filmsWithDefaultCount.size());

        MvcResult resultWithCustomCount = mockMvc.perform(get("/films/popular")
                        .param("count", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Film> filmsWithCustomCount = deserializeList(resultWithCustomCount, Film.class);
        assertEquals(3, filmsWithCustomCount.size());
    }

    private static Integer getRandomElement(List<Integer> list) {
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());

        return list.get(randomIndex);
    }
}
