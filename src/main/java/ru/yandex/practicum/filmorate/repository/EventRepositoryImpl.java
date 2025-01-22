package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.repository.mapper.EventRowMapper;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EventRowMapper eventRowMapper;

    private static final String SELECT_EVENTS_BY_USER_ID = """
            SELECT event_id, timestamp, user_id, event_type, operation, entity_id
            FROM users_feed
            WHERE user_id = ?
            """;
    private static final String INSERT_INTO_USERS_FEED = """
            INSERT INTO users_feed(timestamp, user_id, event_type, operation, entity_id)
            VALUES (?, ?, ?, ?, ?)
            """;

    @Override
    public Collection<Event> getUserFeed(Integer userId) {
        try {
            return jdbcTemplate.queryForStream(SELECT_EVENTS_BY_USER_ID, eventRowMapper, userId).toList();
        } catch (DataAccessException e) {
            final String errorMessage = String.format("Не удалось получить список событий для пользователя id=%d.", userId);
            log.error(errorMessage);
            throw new DataIntegrityViolationException(errorMessage, e.getCause());
        }
    }

    @Override
    public void addEvent(Event event) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement stmt = con.prepareStatement(INSERT_INTO_USERS_FEED, new String[]{"event_id"});
            stmt.setTimestamp(1, event.getTimestamp());
            stmt.setInt(2, event.getUserId());
            stmt.setString(3, event.getEventType().toString());
            stmt.setString(4, event.getOperation().toString());
            stmt.setInt(5, event.getEntityId());
            return stmt;
        };

        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        event.setEventId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        if (event.getEventId() == null) {
            final String errorMessage = "Не удалось сохранить событие: " + event;
            log.error(errorMessage);
            throw new DataIntegrityViolationException(errorMessage);
        }

        log.info("Сохранено событие: {}", event);

    }
}
