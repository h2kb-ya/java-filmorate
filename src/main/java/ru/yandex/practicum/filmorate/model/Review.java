package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@EqualsAndHashCode(exclude = "useful")
public class Review {

    private Integer reviewId;

    @NotBlank(message = "Содержание отзыва не должно быть пустым.")
    @Size(max = 255, message = "Максимальное количество символов: 255.")
    private String content;

    @NotNull(message = "Должен быть указан тип отзыва (true или false).")
    private Boolean isPositive;

    @NotNull(message = "Id пользователя должно быть положительным числом.")
    private Integer userId;

    @NotNull(message = "Id фильма должно быть положительным числом.")
    private Integer filmId;

    private Integer useful;

}
