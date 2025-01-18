package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.OperationTypes;

import java.util.Collection;

public interface EventService {

    void addEvent(Integer userId,
                  EventTypes eventType,
                  OperationTypes operation,
                  Integer entityId);

}
