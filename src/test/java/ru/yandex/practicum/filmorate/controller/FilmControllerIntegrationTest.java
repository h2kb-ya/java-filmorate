package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.model.Film.DURATION_OF_FILM_MUST_BE_POSITIVE;
import static ru.yandex.practicum.filmorate.model.Film.FILM_RELEASE_DATE_MUST_BE_BEFORE_1985;
import static ru.yandex.practicum.filmorate.model.Film.MAX_LENGTH_OF_DESCRIPTION_200_CHARACTERS;
import static ru.yandex.practicum.filmorate.model.Film.NAME_OF_THE_FILM_MUST_NOT_BE_BLANK;

public class FilmControllerIntegrationTest extends AbstractApplicationMvcIntegrationTest {

    @Autowired
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
        filmController.clearFilms();
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

        Film film1 = Film.builder()
                .name("Film 1")
                .description("Film 1 description")
                .duration(3600L)
                .releaseDate(LocalDate.of(2024, 6, 26))
                .build();

        Film film2 = Film.builder()
                .name("Film 2")
                .description("Film 2 description")
                .duration(3600L)
                .releaseDate(LocalDate.of(2024, 6, 26))
                .build();

        filmController.add(film1);
        filmController.add(film2);

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

        Film film = Film.builder()
                .name("Film")
                .description("Film description")
                .duration(3600L)
                .releaseDate(LocalDate.of(2024, 6, 26))
                .build();

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
        Film film = Film.builder()
                .description("Film description")
                .duration(3600L)
                .releaseDate(LocalDate.of(2024, 6, 26))
                .build();

        String content = serialize(film);

        MvcResult result = mockMvc.perform(post("/films")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains(NAME_OF_THE_FILM_MUST_NOT_BE_BLANK));
    }

    @Test
    @SneakyThrows
    void whenAddFilmDescriptionIsTooBigThenReturn400() {
        Film film = Film.builder()
                .name("Film")
                .description("Film description".repeat(20))
                .duration(3600L)
                .releaseDate(LocalDate.of(2024, 6, 26))
                .build();

        String content = serialize(film);

        MvcResult result = mockMvc.perform(post("/films")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains(MAX_LENGTH_OF_DESCRIPTION_200_CHARACTERS));
    }

    @Test
    @SneakyThrows
    void whenAddFilmWrongReleaseDateThenReturn400() {
        Film film = Film.builder()
                .name("Film")
                .description("Film description")
                .duration(3600L)
                .releaseDate(LocalDate.of(1800, 6, 26))
                .build();

        String content = serialize(film);

        MvcResult result = mockMvc.perform(post("/films")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains(FILM_RELEASE_DATE_MUST_BE_BEFORE_1985));
    }

    @Test
    @SneakyThrows
    void whenAddFilmDurationIsNegativeThenReturn400() {
        Film film = Film.builder()
                .name("Film")
                .description("Film description")
                .duration(-3600L)
                .releaseDate(LocalDate.of(2024, 6, 26))
                .build();

        String content = serialize(film);

        MvcResult result = mockMvc.perform(post("/films")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains(DURATION_OF_FILM_MUST_BE_POSITIVE));
    }

}
