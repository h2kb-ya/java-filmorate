package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class DirectorControllerIntegrationTest extends AbstractApplicationMvcIntegrationTest {

    @Autowired
    private DirectorController directorController;

    @Autowired
    private DirectorRepository directorRepository;

    @BeforeEach
    public void setUp() {
        directorRepository.deleteAll();
    }

    private static Director getTestDirector1() {
        return new Director("Кристофер Нолан");
    }

    private static Director getTestDirector2() {
        return new Director("Лилли Вачовски");
    }

    private static Director getTestDirector3() {
        return new Director("Лана Вачовски");
    }

    @Test
    @SneakyThrows
    void whenGetDirectorsDirectorsAreNotExistThenReturnEmptyList() {
        MvcResult result = mockMvc.perform(get("/directors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Director> directors = deserializeList(result, Director.class);
        assertTrue(directors.isEmpty());
    }

    @Test
    @SneakyThrows
    void whenGetDirectorsAreExistThenReturnDirectorsList() {
        assertTrue(directorController.getAll().isEmpty());

        Director director1 = getTestDirector1();
        Director director2 = getTestDirector2();
        Director director3 = getTestDirector3();

        directorController.create(director1);
        directorController.create(director2);
        directorController.create(director3);

        MvcResult result = mockMvc.perform(get("/directors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Director> directors = deserializeList(result, Director.class);
        assertFalse(directors.isEmpty());
        assertEquals(3, directors.size());
    }

    @Test
    @SneakyThrows
    void whenAddDirectorThenReturnDirector() {
        assertTrue(directorController.getAll().isEmpty());

        Director director = getTestDirector1();

        String content = serialize(director);

        MvcResult result = mockMvc.perform(post("/directors")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Director directorFromResponse = deserialize(result, Director.class);
        assertEquals(director.getName(), directorFromResponse.getName());
        assertEquals(1, directorController.getAll().size());
    }

    @Test
    @SneakyThrows
    void whenAddDirectorNameIsEmptyThenReturn400() {
        Director director = new Director("");

        String content = serialize(director);

        MvcResult result = mockMvc.perform(post("/directors")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertTrue(exceptionMessage.contains("Name of the director must not be blank"));
    }
}