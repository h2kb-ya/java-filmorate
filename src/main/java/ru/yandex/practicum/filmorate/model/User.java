package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private int id;

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

    @Setter(AccessLevel.NONE)
    private Set<Integer> friends = new HashSet<>();

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            this.name = this.login;
        } else {
            this.name = name;
        }
    }

    public void addFriend(Integer userId) {
        friends.add(userId);
    }

    public void removeFriend(Integer userId) {
        friends.remove(userId);
    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        setName(name);
        this.birthday = birthday;
    }
}
