package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Director {

    private Integer id;

    @NotBlank(message = "Name of the director must not be blank")
    private String name;

    public Director(String name) {
        this.name = name;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", getName());
        return values;
    }
}