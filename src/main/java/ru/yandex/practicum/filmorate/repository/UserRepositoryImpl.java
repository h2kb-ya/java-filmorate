package ru.yandex.practicum.filmorate.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.UserExtractor;
import ru.yandex.practicum.filmorate.repository.mapper.UsersExtractors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final UserExtractor userExtractor;
    private final UsersExtractors usersExtractors;

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (:email, :login, :name, :birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        log.info("Creating new user {}", user);
        namedParameterJdbcOperations.update(sqlQuery, new MapSqlParameterSource(user.toMap()), keyHolder);

        Integer id = null;
        if (keyHolder.getKey() != null) {
            id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        }

        if (id != null) {
            user.setId(id);
        } else {
            throw new DataIntegrityViolationException("Не удалось сохранить пользователя: " + user);
        }

        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET email = ?, name = ?, login = ?, birthday = ? WHERE id = ?";

        log.info("Updating user {}", user);
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getName(), user.getLogin(), user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public void delete(User user) {
        String sqlQuery = "DELETE FROM users WHERE id = ?";

        log.info("Deleting user {}", user);
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public void deleteById(Integer userId) {
        String sqlQuery = "DELETE FROM users WHERE id = ?";

        log.info("Deleting user with ID - {}", userId);
        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public void deleteAll() {
        String sqlQuery = "DELETE FROM users";

        log.info("Deleting all users");
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = """
                SELECT u.id AS user_id, u.email AS user_email, u.name AS user_name, u.login AS user_login,
                        u.birthday AS user_birthday
                FROM users u
                """;

        log.info("Finding all users");
        return jdbcTemplate.query(sqlQuery, usersExtractors);
    }

    @Override
    public Optional<User> findById(Integer id) {
        String sqlQuery = """
                SELECT u.id AS user_id, u.email AS user_email, u.name AS user_name, u.login AS user_login,
                        u.birthday AS user_birthday
                FROM users u
                WHERE id = ?
                """;

        log.info("Getting user by id {}", id);
        return Optional.ofNullable(jdbcTemplate.query(sqlQuery, userExtractor, id));
    }
}
