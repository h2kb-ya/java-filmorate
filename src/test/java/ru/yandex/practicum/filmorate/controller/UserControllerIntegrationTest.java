package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.model.User.BIRTHDAY_DATE_MUST_NOT_BE_IN_THE_FUTURE;
import static ru.yandex.practicum.filmorate.model.User.EMAIL_MUST_NOT_BE_BLANK;
import static ru.yandex.practicum.filmorate.model.User.ILLEGAL_FORMAT_OF_EMAIL_ADDRESS;
import static ru.yandex.practicum.filmorate.model.User.LOGIN_MUST_NOT_BE_BLANK;
import static ru.yandex.practicum.filmorate.model.User.LOGIN_MUST_NOT_CONTAIN_WHITE_SPACES;

import java.time.LocalDate;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.model.User;

public class UserControllerIntegrationTest extends AbstractApplicationMvcIntegrationTest {

    @Autowired
    private UserController userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userController.clearUsers();
    }

    @Test
    @SneakyThrows
    void whenGetUsersUsersAreNotExistThenReturnEmptyList() {
        MvcResult result = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<User> users = deserializeList(result, User.class);
        assertTrue(users.isEmpty());
    }

    @Test
    @SneakyThrows
    void whenGetUsersUsersAreExistThenReturnUsersList() {
        assertTrue(userController.getUsers().isEmpty());

        User user1 = new User("user1@yandex.ru", "user1", "User1", LocalDate.of(1985, 1, 1));
        User user2 = new User("user2@yandex.ru", "user2", "User2", LocalDate.of(1985, 1, 1));

        userController.create(user1);
        userController.create(user2);

        MvcResult result = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<User> users = deserializeList(result, User.class);
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
    }

    @Test
    @SneakyThrows
    void whenCreateUserThenReturnUser() {
        assertTrue(userController.getUsers().isEmpty());

        User user = new User("user@yandex.ru", "user", "User", LocalDate.of(1985, 1, 1));
        String content = serialize(user);

        MvcResult result = mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponse = deserialize(result, User.class);
        assertEquals(user.getName(), userFromResponse.getName());
        assertEquals(user.getLogin(), userFromResponse.getLogin());
        assertEquals(1, userController.getUsers().size());
    }

    @Test
    @SneakyThrows
    void whenCreateUserNameIsEmptyThenReturnUserWithLoginAsName() {
        User user = new User("user@yandex.ru", "user", null, LocalDate.of(1985, 1, 1));
        String content = serialize(user);

        MvcResult result = mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        User userFromResponse = deserialize(result, User.class);
        assertEquals(user.getLogin(), userFromResponse.getName());
        assertEquals(user.getLogin(), userFromResponse.getLogin());
        assertEquals(1, userController.getUsers().size());
    }

    @Test
    @SneakyThrows
    void whenCreateUserEmailIsWrongThenReturn400() {
        User user = new User("useryandex.ru@---", "user", "User", LocalDate.of(1985, 1, 1));
        String content = serialize(user);

        MvcResult result = mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains(ILLEGAL_FORMAT_OF_EMAIL_ADDRESS));
    }

    @Test
    @SneakyThrows
    void whenCreateUserEmailIsBlankThenReturn400() {
        User user = new User(" ", "user", "User", LocalDate.of(1985, 1, 1));
        String content = serialize(user);

        MvcResult result = mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains(EMAIL_MUST_NOT_BE_BLANK));
    }

    @Test
    @SneakyThrows
    void whenCreateUserLoginIsBlankThenReturn400() {
        User user = new User("user@yandex.ru", " ", "User", LocalDate.of(1985, 1, 1));
        String content = serialize(user);

        MvcResult result = mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains(LOGIN_MUST_NOT_BE_BLANK));
    }

    @Test
    @SneakyThrows
    void whenCreateUserLoginHasWhiteSpacesThenReturn400() {
        User user = new User("user@yandex.ru", "login login", "User", LocalDate.of(1985, 1, 1));
        String content = serialize(user);

        MvcResult result = mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains(LOGIN_MUST_NOT_CONTAIN_WHITE_SPACES));
    }

    @Test
    @SneakyThrows
    void whenCreateUserBirthdayIsInTheFutureThenReturn400() {
        User user = new User("user@yandex.ru", "login login", "User", LocalDate.of(2025, 1, 1));
        String content = serialize(user);

        MvcResult result = mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains(BIRTHDAY_DATE_MUST_NOT_BE_IN_THE_FUTURE));
    }
}
