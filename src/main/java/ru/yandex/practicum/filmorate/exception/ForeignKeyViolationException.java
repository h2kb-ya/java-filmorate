package ru.yandex.practicum.filmorate.exception;

public class ForeignKeyViolationException extends RuntimeException {

    public ForeignKeyViolationException(String message) {
        super(message);
    }
}
