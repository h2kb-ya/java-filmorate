package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIntegrationTest extends AbstractApplicationMvcIntegrationTest {

    @Autowired
    private UserController userController;

    @Autowired
    @Qualifier("userRepositoryImpl")
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
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
        assertTrue(exceptionMessage.contains("Illegal format of email address"));
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
        assertTrue(exceptionMessage.contains("Email must not be blank"));
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
        assertTrue(exceptionMessage.contains("Login must not be blank"));
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
        assertTrue(exceptionMessage.contains("Login must not contain white spaces"));
    }

    @Test
    @SneakyThrows
    void whenCreateUserBirthdayIsInTheFutureThenReturn400() {
        User user = new User("user@yandex.ru", "user", "User",
                LocalDate.now().plusYears(1).withMonth(1).withDayOfMonth(1));
        String content = serialize(user);

        MvcResult result = mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String exceptionMessage = result.getResolvedException().getMessage();
        assertTrue(exceptionMessage.contains("Birthday date must not be in the future"));
    }

    @Test
    @SneakyThrows
    void whenAddUserAsFriend_UserAndFriendExist_ThenReturnUserFriendList() {
        assertTrue(userController.getUsers().isEmpty());

        User user1 = new User("user1@yandex.ru", "user1", "User1", LocalDate.of(1985, 1, 1));
        User user2 = new User("user2@yandex.ru", "user2", "User2", LocalDate.of(1985, 1, 1));

        User savedUser1 = userController.create(user1);
        User savedUser2 = userController.create(user2);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", savedUser1.getId(), savedUser2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{id}/friends", savedUser1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(savedUser2.getId()));
    }

    @Test
    @SneakyThrows
    void whenRemoveUserAsFriend_UserAndFriendExist_ThenReturnEmptyList() {
        assertTrue(userController.getUsers().isEmpty());

        User user1 = new User("user1@yandex.ru", "user1", "User1", LocalDate.of(1985, 1, 1));
        User user2 = new User("user2@yandex.ru", "user2", "User2", LocalDate.of(1985, 1, 1));

        User savedUser1 = userController.create(user1);
        User savedUser2 = userController.create(user2);
        userController.addFriend(savedUser1.getId(), savedUser2.getId());

        mockMvc.perform(delete("/users/{id}/friends/{friendId}", savedUser1.getId(), savedUser2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{id}/friends", savedUser1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(get("/users/{id}/friends", savedUser2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @SneakyThrows
    void whenAddUserAsFriend_UsersDoesNotExist_ThenReturnNotFound() {
        assertTrue(userController.getUsers().isEmpty());

        int badUserId = 999;
        User friend = new User("user2@yandex.ru", "user2", "User2", LocalDate.of(1985, 1, 1));
        User savedUser = userController.create(friend);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", badUserId, savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Объект не найден"))
                .andExpect(jsonPath("$.description").value("Пользователь c id " + badUserId + " не найден."));
    }

    @Test
    @SneakyThrows
    void whenAddUserAsFriend_friendsDoesNotExist_ThenReturnNotFound() {
        assertTrue(userController.getUsers().isEmpty());

        int badFriendId = 999;
        User user = new User("user2@yandex.ru", "user2", "User2", LocalDate.of(1985, 1, 1));
        User savedUser = userController.create(user);

        mockMvc.perform(put("/users/{id}/friends/{friendId}", savedUser.getId(), badFriendId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Объект не найден"))
                .andExpect(jsonPath("$.description").value("Пользователь c id " + badFriendId + " не найден."));
    }

    @Test
    @SneakyThrows
    void whenGetCommonFriends_UserAndFriendExist_ThenReturnCommonFriendsList() {
        assertTrue(userController.getUsers().isEmpty());

        User user1 = new User("user1@yandex.ru", "user1", "User1", LocalDate.of(1985, 1, 1));
        User user2 = new User("user2@yandex.ru", "user2", "User2", LocalDate.of(1985, 1, 1));
        User user3 = new User("user3@yandex.ru", "user3", "User3", LocalDate.of(1985, 1, 1));

        User savedUser1 = userController.create(user1);
        User savedUser2 = userController.create(user2);
        User savedUser3 = userController.create(user3);
        userController.addFriend(savedUser1.getId(), savedUser3.getId());
        userController.addFriend(savedUser2.getId(), savedUser3.getId());

        mockMvc.perform(get("/users/{id}/friends", savedUser1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(savedUser3.getId()));

        mockMvc.perform(get("/users/{id}/friends", savedUser2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(savedUser3.getId()));
    }
}
