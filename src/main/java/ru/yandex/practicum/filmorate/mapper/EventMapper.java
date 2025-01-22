package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.model.Event;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static EventDto mapToEventDto(Event event) {

        return EventDto.builder()
                .timestamp(event.getTimestamp().toInstant().toEpochMilli())
                .userId(event.getUserId())
                .eventType(event.getEventType().toString().toUpperCase())
                .operation(event.getOperation().toString().toUpperCase())
                .eventId(event.getEventId())
                .entityId(event.getEntityId())
                .build();

    }

}
