package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DirectorDto {

    private Integer id;

    @NotBlank(message = "Name of the director must not be blank")
    private String name;
}
