package ru.yandex.practicum.filmorate.dto;

import lombok.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FilmDto {

    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;
    private int likes;
    private Set<Genre> genres;
    private Set<Director> directors;
    private Mpa mpa;
}
