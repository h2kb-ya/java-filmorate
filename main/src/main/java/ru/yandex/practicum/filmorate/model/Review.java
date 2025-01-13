package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@EqualsAndHashCode(exclude = "useful")
public class Review {

    @Positive(message = "Review id must be positive.")
    private Integer reviewId;

    @NotBlank(message = "Review content must not be blank.")
    @Size(max = 255, message = "Max length of review content - 255 characters")
    private String content;

    @NotNull(message = "Review mark must exist.")
    private Boolean isPositive;

    @Positive(message = "User id must be positive.")
    private Integer userId;

    @Positive(message = "Film id must be positive.")
    private Integer filmId;

    @Digits(message = "Useful level must me digit.", integer = 6, fraction = 0)
    private Integer useful;

}
