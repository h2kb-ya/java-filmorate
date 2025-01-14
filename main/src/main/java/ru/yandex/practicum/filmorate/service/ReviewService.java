package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewService {

    Review addNewReview(Review newReview);
    Review updateCurrentReview(Review currentReview);
    Review deleteCurrentReview(Integer reviewId);
    Review getReviewById(Integer reviewId);
    Collection<Review> getReviewsList(Integer filmId, Integer count);
    Review putLikeToReview(Integer reviewId, Integer userId);
    Review putDislikeToReview(Integer reviewId, Integer userId);
    void deleteLikeFromReview(Integer reviewId, Integer userId);
    void deleteDislikeFromReview(Integer reviewId, Integer userId);

}
