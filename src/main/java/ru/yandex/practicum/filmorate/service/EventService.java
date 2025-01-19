package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.OperationTypes;

public interface EventService {

    void addEvent(Integer userId,
                  EventTypes eventType,
                  OperationTypes operation,
                  Integer entityId);

}
