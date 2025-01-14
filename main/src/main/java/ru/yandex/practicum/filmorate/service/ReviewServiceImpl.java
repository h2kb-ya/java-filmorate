package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;

import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.util.FilmorateConstants.DEFAULT_REVIEW_USEFUL_RATE;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewServiceImpl implements ReviewService {

    @Qualifier("reviewRepositoryImpl")
    private final ReviewRepository reviewRepository;
    private final FilmService filmService;
    private final UserService userService;

    @Override
    public Review addNewReview(Review newReview) {

        if (userService.get(newReview.getUserId()) == null) {
            final String errorMessage = String.format("Пользователь с id=%d не найден.", newReview.getUserId());
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        if (filmService.getFilm(newReview.getFilmId()) == null) {
            final String errorMessage = String.format("Фильм с id=%d не найден.", newReview.getFilmId());
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        newReview.setUseful(DEFAULT_REVIEW_USEFUL_RATE);

        final Review review = reviewRepository.addNewReview(newReview);
        log.info("Добавлен отзыв: {}", review);
        return review;
    }

    @Override
    public Review updateCurrentReview(Review currentReview) {
        getReviewById(currentReview.getReviewId());
        final Review updatedReview = reviewRepository.updateCurrentReview(currentReview);
        log.info("Обновлен отзыв: {}", updatedReview);
        return updatedReview;
    }

    @Override
    public Review deleteCurrentReview(Integer reviewId) {
        final Review review = getReviewById(reviewId);
        reviewRepository.deleteCurrentReview(reviewId);
        log.info("Удален отзыв: {}", review);
        return review;
    }

    @Override
    public Review getReviewById(Integer reviewId) {
        final Review review = reviewRepository.getReviewById(reviewId);

        if (review == null) {
            final String errorMessage = String.format("Отзыв с id=%d не найден.", reviewId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        return review;
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
    public void deleteLikeFromReview(Integer reviewId, Integer userId) {

    }

    @Override
    public void deleteDislikeFromReview(Integer reviewId, Integer userId) {

    }

}
