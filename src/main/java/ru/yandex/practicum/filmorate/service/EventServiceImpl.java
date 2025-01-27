package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.OperationTypes;
import ru.yandex.practicum.filmorate.repository.EventRepository;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    @Qualifier("eventRepositoryImpl")
    private final EventRepository eventRepository;

    @Override
    public void addEvent(Integer userId, EventTypes eventType, OperationTypes operation, Integer entityId) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        Event newEvent = Event.builder()
                .timestamp(now)
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();

        eventRepository.addEvent(newEvent);
    }

}
