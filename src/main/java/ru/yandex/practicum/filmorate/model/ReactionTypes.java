package ru.yandex.practicum.filmorate.model;

public enum ReactionTypes {
    LIKE("like"),
    DISLIKE("dislike");

    private final String type;

    ReactionTypes(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

}
