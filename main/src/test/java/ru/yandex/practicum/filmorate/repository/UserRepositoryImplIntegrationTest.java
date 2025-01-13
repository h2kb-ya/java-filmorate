package ru.yandex.practicum.filmorate.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.UserExtractor;
import ru.yandex.practicum.filmorate.repository.mapper.UserMapper;
import ru.yandex.practicum.filmorate.repository.mapper.UsersExtractors;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepositoryImpl.class, UserExtractor.class, UsersExtractors.class, UserMapper.class})
public class UserRepositoryImplIntegrationTest {

    private final UserRepositoryImpl userRepositoryImpl;

    private static User getTestUser1() {
        User user = new User("TestUser1@example.com", "TestUser1", "Test User One", LocalDate.of(1990, 5, 15));
        user.setId(1);

        return user;
    }

    private static User getTestUser2() {
        User user = new User("TestUser2@example.com", "TestUser2", "Test User Two", LocalDate.of(1990, 5, 15));
        user.setId(2);

        return user;
    }

    private static User getTestUser3() {
        User user = new User("TestUser3@example.com", "TestUser3", "Test User Three", LocalDate.of(1990, 5, 15));
        user.setId(3);

        return user;
    }

    private static User getTestUser4() {
        User user = new User("TestUser4@example.com", "TestUser4", "Test User Four", LocalDate.of(1990, 5, 15));
        user.setId(4);

        return user;
    }

    @Test
    public void findAll_usersExist_returnUser() {
        List<User> users = userRepositoryImpl.findAll();
        assertThat(users)
                .isNotEmpty()
                .hasSize(4)
                .allMatch(Objects::nonNull)
                .contains(getTestUser1(), getTestUser2(), getTestUser3(), getTestUser4());
    }

    @Test
    public void findById_userExists_returnOptionalUser() {
        Optional<User> user = userRepositoryImpl.findById(getTestUser1().getId());
        assertThat(user).isNotEmpty();
        assertThat(user.get()).isEqualTo(getTestUser1());
    }

    @Test
    public void findById_userDoesNotExist_returnOptionalEmpty() {
        Optional<User> user = userRepositoryImpl.findById(999);
        assertThat(user).isEmpty();
    }

    @Test
    public void create_happyPath_returnCreatedUser() {
        User user = new User("TestUser@example.com", "TestUser", "Test User", LocalDate.of(1990, 5, 15));

        Integer createdUserId = userRepositoryImpl.create(user).getId();
        user.setId(createdUserId);

        assertThat(userRepositoryImpl.findById(createdUserId).get()).isEqualTo(user);
    }

    @Test
    public void update_happyPath_returnUpdatedUser() {
        User user = getTestUser1();
        user.setName("Updated Test Name");

        userRepositoryImpl.update(user);

        assertThat(userRepositoryImpl.findById(user.getId()).get()).isEqualTo(user);
    }

    @Test
    public void delete_userExists_userDeleted() {
        Optional<User> existedUser = userRepositoryImpl.findById(getTestUser1().getId());
        assertThat(existedUser).isNotEmpty();

        userRepositoryImpl.delete(getTestUser1());
        existedUser = userRepositoryImpl.findById(getTestUser1().getId());
        assertThat(existedUser).isEmpty();
    }

    @Test
    public void deleteAll_usersExist_usersDeleted() {
        List<User> users = userRepositoryImpl.findAll();
        assertThat(users.size()).isEqualTo(4);

        userRepositoryImpl.deleteAll();
        users = userRepositoryImpl.findAll();
        assertThat(users).isEmpty();
    }
}
