package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewRepository {

    Review addNewReview(Review newReview);
    Review updateCurrentReview(Review currentReview);
    Review deleteCurrentReview(Integer reviewId);
    Review getReviewById(Integer reviewId);
    Collection<Review> getReviewsList(Integer count);
    Collection<Review> getReviewsListByFilmId(Integer filmId, Integer count);
    Review putLikeToReview(Integer reviewId);
    Review putDislikeToReview(Integer reviewId);
    Review deleteLikeFromReview(Integer reviewId);
    Review deleteDislikeFromReview(Integer reviewId);

}
