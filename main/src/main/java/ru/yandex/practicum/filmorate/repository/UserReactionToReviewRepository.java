package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.UserReactionToReview;

public interface UserReactionToReviewRepository {

    UserReactionToReview addNewReaction(UserReactionToReview reaction);
    UserReactionToReview getReaction(Integer userId, Integer reviewId);
    UserReactionToReview updateReaction(UserReactionToReview reaction);
    void deleteReaction(UserReactionToReview reaction);

}
