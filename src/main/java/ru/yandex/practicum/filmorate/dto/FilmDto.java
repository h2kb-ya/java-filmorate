package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Name of the film must not be blank")
    private String name;

    @Size(max = 200, message = "Max length of description - 200 characters")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Duration of film must be positive")
    private Long duration;

    private int likes;

    private Set<Genre> genres;
    private Set<Director> directors;

    private Mpa mpa;
}
