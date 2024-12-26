package ru.yandex.practicum.filmorate.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserMapper {

    public User mapUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String email = rs.getString("user_email");
        String login = rs.getString("user_login");
        String name = rs.getString("user_name");
        LocalDate birthday = rs.getDate("user_birthday").toLocalDate();

        User user = new User(email, login, name, birthday);
        user.setId(id);

        return user;
    }
}
