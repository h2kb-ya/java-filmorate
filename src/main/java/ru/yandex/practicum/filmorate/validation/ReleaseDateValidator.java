package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateAfter1985, LocalDate> {

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext context) {
        if (releaseDate == null) {
            return false;
        }

        return releaseDate.isAfter(LocalDate.of(1985, 12, 28));
    }
}
