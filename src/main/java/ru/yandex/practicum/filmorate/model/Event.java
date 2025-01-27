package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
public class Event {

    private Long eventId;
    private Timestamp timestamp;
    private Integer userId;
    private EventTypes eventType;
    private OperationTypes operation;
    private Integer entityId;

}
