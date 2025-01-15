package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@EqualsAndHashCode
public class UserReactionToReview {

    private Long reactionId;
    private Integer userId;
    private Integer reviewId;
    private String reaction;

}
