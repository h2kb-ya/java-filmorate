package ru.yandex.practicum.filmorate.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
