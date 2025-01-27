package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;

public interface EventRepository {

    Collection<Event> getUserFeed(Integer userId);

    void addEvent(Event event);

}
