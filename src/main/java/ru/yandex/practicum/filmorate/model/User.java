package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * User
 */
@Getter
public class User {

    public static final String EMAIL_MUST_NOT_BE_BLANK = "Email must not be blank";
    public static final String ILLEGAL_FORMAT_OF_EMAIL_ADDRESS = "Illegal format of email address";
    public static final String LOGIN_MUST_NOT_BE_BLANK = "Login must not be blank";
    public static final String LOGIN_MUST_NOT_CONTAIN_WHITE_SPACES = "Login must not contain white spaces";
    public static final String BIRTHDAY_DATE_MUST_NOT_BE_IN_THE_FUTURE = "Birthday date must not be in the future";

    @Setter
    int id;

    @NotBlank(message = EMAIL_MUST_NOT_BE_BLANK)
    @Email(message = ILLEGAL_FORMAT_OF_EMAIL_ADDRESS)
    @Setter
    String email;

    @NotBlank(message = LOGIN_MUST_NOT_BE_BLANK)
    @Pattern(regexp = "\\S+", message = LOGIN_MUST_NOT_CONTAIN_WHITE_SPACES)
    @Setter
    String login;

    String name;

    @PastOrPresent(message = BIRTHDAY_DATE_MUST_NOT_BE_IN_THE_FUTURE)
    @Setter
    LocalDate birthday;

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
