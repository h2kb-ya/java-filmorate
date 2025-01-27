package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventDto {

    private Long timestamp;
    private Integer userId;
    private String eventType;
    private String operation;
    private Long eventId;
    private Integer entityId;

}
