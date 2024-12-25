package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {

    private Integer id;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Illegal format of email address")
    private String email;

    @NotBlank(message = "Login must not be blank")
    @Pattern(regexp = "\\S+", message = "Login must not contain white spaces")
    private String login;

    @Setter(AccessLevel.NONE)
    private String name;

    @PastOrPresent(message = "Birthday date must not be in the future")
    private LocalDate birthday;

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            this.name = this.login;
        } else {
            this.name = name;
        }
    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        setName(name);
        this.birthday = birthday;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", getEmail());
        values.put("login", getLogin());
        values.put("name", getName());
        values.put("birthday", getBirthday());

        return values;
    }
}
