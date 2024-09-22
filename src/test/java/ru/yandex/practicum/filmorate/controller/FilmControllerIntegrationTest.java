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
        assertTrue(exceptionMessage.contains("Name of the film must not be blank"));
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
        assertTrue(exceptionMessage.contains("Max length of description - 200 characters"));
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
        assertTrue(exceptionMessage.contains("Film release date must be after 1895"));
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
        assertTrue(exceptionMessage.contains("Duration of film must be positive"));
    }

}
