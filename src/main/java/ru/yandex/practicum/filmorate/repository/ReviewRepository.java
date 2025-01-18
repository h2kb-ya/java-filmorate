package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

public interface ReviewRepository {

    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteReview(Integer reviewId);

    Review getReview(Integer reviewId);

    Collection<Review> getReviews(Integer filmId, Integer count);

}
