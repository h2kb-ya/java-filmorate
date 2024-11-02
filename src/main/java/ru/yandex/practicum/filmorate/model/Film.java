package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.validation.ReleaseDateAfter1895;

@Getter
@Setter
@Builder
public class Film {

    private Integer id;

    @NotBlank(message = "Name of the film must not be blank")
    private String name;

    @Size(max = 200, message = "Max length of description - 200 characters")
    private String description;

    @ReleaseDateAfter1895(message = "Film release date must be after 1895")
    private LocalDate releaseDate;

    @Positive(message = "Duration of film must be positive")
    private Long duration;

    @Setter(AccessLevel.NONE)
    private Set<Integer> likes;

    public void like(Integer userId) {
        likes.add(userId);
    }

    public void dislike(Integer userId) {
        likes.remove(userId);
    }
}
