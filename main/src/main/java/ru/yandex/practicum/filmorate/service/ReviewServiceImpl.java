package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    @Override
    public Review addNewReview(Review newReview) {
        return null;
    }

    @Override
    public Review updateCurrentReview(Review currentReview) {
        return null;
    }

    @Override
    public Review deleteCurrentReview(Integer reviewId) {
        return null;
    }

    @Override
    public Review getReviewById(Integer reviewId) {
        return null;
    }

    @Override
    public Collection<Review> getReviewsListByFilmId(Integer filmId, Integer count) {
        return List.of();
    }

    @Override
    public Review putLikeToReview(Integer reviewId, Integer userId) {
        return null;
    }

    @Override
    public Review putDislikeToReview(Integer reviewId, Integer userId) {
        return null;
    }

    @Override
    public Review deleteLikeFromReview(Integer reviewId, Integer userId) {
        return null;
    }

    @Override
    public Review deleteDislikeFromReview(Integer reviewId, Integer userId) {
        return null;
    }

}
