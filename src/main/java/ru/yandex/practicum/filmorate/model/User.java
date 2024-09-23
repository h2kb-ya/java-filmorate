package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
public class User {

    @Setter
    private int id;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Illegal format of email address")
    @Setter
    private String email;

    @NotBlank(message = "Login must not be blank")
    @Pattern(regexp = "\\S+", message = "Login must not contain white spaces")
    @Setter
    private String login;

    private String name;

    @PastOrPresent(message = "Birthday date must not be in the future")
    @Setter
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
}
