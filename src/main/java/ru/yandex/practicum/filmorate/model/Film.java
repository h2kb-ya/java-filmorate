package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.validation.ReleaseDateAfter1985;

/**
 * Film
 */
@Getter
@Setter
@Builder
public class Film {

    public static final String NAME_OF_THE_FILM_MUST_NOT_BE_BLANK = "Name of the film must not be blank";
    public static final String MAX_LENGTH_OF_DESCRIPTION_200_CHARACTERS = "Max length of description - 200 characters";
    public static final String DURATION_OF_FILM_MUST_BE_POSITIVE = "Duration of film must be positive";
    public static final String FILM_RELEASE_DATE_MUST_BE_AFTER_1985 = "Film release date must be after 1985";

    int id;

    @NotBlank(message = NAME_OF_THE_FILM_MUST_NOT_BE_BLANK)
    String name;

    @Size(max = 200, message = MAX_LENGTH_OF_DESCRIPTION_200_CHARACTERS)
    String description;

    @ReleaseDateAfter1985(message = FILM_RELEASE_DATE_MUST_BE_AFTER_1985)
    LocalDate releaseDate;

    @Positive(message = DURATION_OF_FILM_MUST_BE_POSITIVE)
    Long duration;
}
