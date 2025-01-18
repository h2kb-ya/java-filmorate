package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.OperationTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventRowMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        return Event.builder()
                .eventId(resultSet.getLong("event_id"))
                .timestamp(resultSet.getTimestamp("timestamp"))
                .userId(resultSet.getInt("user_id"))
                .eventType(EventTypes.valueOf(resultSet.getString("event_type")))
                .operation(OperationTypes.valueOf(resultSet.getString("operation")))
                .entityId(resultSet.getInt("entity_id"))
                .build();

    }

}
