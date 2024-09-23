package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.validation.ReleaseDateAfter1895;

@Getter
@Setter
@Builder
public class Film {

    private int id;

    @NotBlank(message = "Name of the film must not be blank")
    private String name;

    @Size(max = 200, message = "Max length of description - 200 characters")
    private String description;

    @ReleaseDateAfter1895(message = "Film release date must be after 1895")
    private LocalDate releaseDate;

    @Positive(message = "Duration of film must be positive")
    private Long duration;
}
