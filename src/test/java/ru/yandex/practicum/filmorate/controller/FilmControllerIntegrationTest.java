package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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
    private FilmStorage filmStorage;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    UserService userService;

    @BeforeEach
    public void setUp() {
        filmStorage.deleteAllFilms();
        userStorage.deleteAllUsers();
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

        Film film1 = new Film("Film 1", "Film 1 description", LocalDate.of(2024, 6, 26), 3600L);
        Film film2 = new Film("Film 2", "Film 2 description", LocalDate.of(2024, 6, 26), 3600L);

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

        Film film = new Film("Film", "Film description", LocalDate.of(2024, 6, 26), 3600L);

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
        User savedUser = userService.create(user);
        Film film = new Film("Film", "Film description", LocalDate.of(2024, 6, 26), -3600L);
        Film savedFilm = filmController.create(film);

        assertTrue(savedFilm.getLikes().isEmpty());

        mockMvc.perform(put("/films/{id}/like/{userId}", savedFilm.getId(), savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertTrue(filmStorage.getFilm(savedFilm.getId()).getLikes().contains(savedUser.getId()));
    }

    @Test
    @SneakyThrows
    void addLike_filmDoesNotExist_returnNotFound() {
        assertTrue(filmController.getFilms().isEmpty());

        User user = new User("user@yandex.ru", "user", "User", LocalDate.of(1985, 1, 1));
        User savedUser = userService.create(user);
        int badFilmId = 999;

        mockMvc.perform(put("/films/{id}/like/{userId}", badFilmId, savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Объект не найден"))
                .andExpect(jsonPath("$.description").value("Фильм c id " + badFilmId + " не найден"));
    }

    @Test
    @SneakyThrows
    void addLike_userDoesNotExist_returnNotFound() {
        assertTrue(filmController.getFilms().isEmpty());

        int badUserId = 999;
        Film film = new Film("Film", "Film description", LocalDate.of(2024, 6, 26), -3600L);
        Film savedFilm = filmController.create(film);

        mockMvc.perform(put("/films/{id}/like/{userId}", savedFilm.getId(), badUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Объект не найден"))
                .andExpect(jsonPath("$.description").value("Пользователь c id " + badUserId + " не найден"));
    }

    @Test
    @SneakyThrows
    void removeLike_filmExists_returnEmptyLikes() {
        assertTrue(filmController.getFilms().isEmpty());

        User user = new User("user@yandex.ru", "user", "User", LocalDate.of(1985, 1, 1));
        User savedUser = userService.create(user);
        Film film = new Film("Film", "Film description", LocalDate.of(2024, 6, 26), -3600L);
        Film savedFilm = filmController.create(film);

        assertTrue(savedFilm.getLikes().isEmpty());

        mockMvc.perform(put("/films/{id}/like/{userId}", savedFilm.getId(), savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertTrue(filmStorage.getFilm(savedFilm.getId()).getLikes().contains(savedUser.getId()));

        mockMvc.perform(delete("/films/{id}/like/{userId}", savedFilm.getId(), savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertFalse(filmStorage.getFilm(savedFilm.getId()).getLikes().contains(savedUser.getId()));
    }

    @Test
    @SneakyThrows
    void getPopular_happyPath_returnRightCountOfPopularFilms() {
        assertTrue(filmController.getFilms().isEmpty());

        User user = new User("user@yandex.ru", "user", "User", LocalDate.of(1985, 1, 1));
        IntStream.range(0, 20).forEach(i -> userService.create(user));
        Film film = new Film("Film", "Film description", LocalDate.of(2024, 6, 26), -3600L);
        IntStream.range(0, 20).forEach(i -> filmController.create(film));

        List<Integer> userIds = userService.getUsers().stream()
                .map(User::getId)
                .toList();

        List<Integer> filmIds = filmController.getFilms().stream()
                .map(Film::getId)
                .toList();

        IntStream.range(0, 100).forEach(i -> filmController.like(getRandomElement(filmIds), getRandomElement(userIds)));

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
