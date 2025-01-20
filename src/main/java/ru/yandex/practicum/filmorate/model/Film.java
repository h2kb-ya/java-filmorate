package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.filmorate.validation.ReleaseDateAfter1895;

@Getter
@Setter
@EqualsAndHashCode
@ToString
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

    private int likes;

    private Set<Genre> genres = new HashSet<>();

    private Mpa mpa;

    public Film(String name, String description, LocalDate releaseDate, Long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", getName());
        values.put("description", getDescription());
        values.put("release_date", getReleaseDate());
        values.put("duration", getDuration());
        values.put("mpa_rating_id", getMpa().getId());

        return values;
    }
}
