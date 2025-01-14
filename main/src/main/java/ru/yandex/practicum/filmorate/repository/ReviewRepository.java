package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewRepository {

    Review addNewReview(Review newReview);
    Review updateCurrentReview(Review currentReview);
    void deleteCurrentReview(Integer reviewId);
    Review getReviewById(Integer reviewId);
    Collection<Review> getReviewsList(Integer count);
    Collection<Review> getReviewsListByFilmId(Integer filmId, Integer count);

}
