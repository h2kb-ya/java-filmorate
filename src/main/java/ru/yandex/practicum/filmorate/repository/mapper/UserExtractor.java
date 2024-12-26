package ru.yandex.practicum.filmorate.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
@RequiredArgsConstructor
public class UserExtractor implements ResultSetExtractor<User> {

    private final UserMapper userMapper;

    @Override
    public User extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) {
            return null;
        }

        User user = userMapper.mapUser(rs);

        return user;
    }
}
