package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataDuplicationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.repository.ReviewRepository;
import ru.yandex.practicum.filmorate.repository.UserReactionToReviewRepository;

import java.util.Collection;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.util.FilmorateConstants.DEFAULT_REVIEW_USEFUL_RATE;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewServiceImpl implements ReviewService {

    @Qualifier("reviewRepositoryImpl")
    private final ReviewRepository reviewRepository;

    @Qualifier("userReactionToReviewRepositoryImpl")
    private final UserReactionToReviewRepository userReactionToReviewRepository;

    private final FilmService filmService;
    private final UserService userService;
    private final EventService eventService;

    @Override
    public Review addNewReview(Review newReview) {
        userService.get(newReview.getUserId());
        filmService.getFilm(newReview.getFilmId());
        newReview.setUseful(DEFAULT_REVIEW_USEFUL_RATE);
        final Review review = reviewRepository.addReview(newReview);
        log.info("Добавлен отзыв: {}", review);
        eventService.addEvent(review.getUserId(), EventTypes.REVIEW, OperationTypes.ADD, review.getReviewId());
        return review;
    }

    @Override
    public Review updateCurrentReview(Review review) {
        final Review currentReview = getReviewById(review.getReviewId());
        review.setUseful(currentReview.getUseful());
        final Review updatedReview = reviewRepository.updateReview(review);
        log.info("Обновлен отзыв: {}", updatedReview);
        eventService.addEvent(updatedReview.getUserId(), EventTypes.REVIEW, OperationTypes.UPDATE, updatedReview.getReviewId());
        return updatedReview;
    }

    @Override
    public Review deleteCurrentReview(Integer reviewId) {
        final Review review = getReviewById(reviewId);
        reviewRepository.deleteReview(reviewId);
        log.info("Удален отзыв: {}", review);
        eventService.addEvent(review.getUserId(), EventTypes.REVIEW, OperationTypes.REMOVE, reviewId);
        return review;
    }

    @Override
    public Review getReviewById(Integer reviewId) {
        final Review review = reviewRepository.getReview(reviewId);
        if (review == null) {
            final String errorMessage = String.format("Отзыв с id=%d не найден.", reviewId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return review;
    }

    @Override
    public Collection<Review> getReviewsList(Integer filmId, Integer count) {
        return reviewRepository.getReviews(filmId, count);
    }

    @Override
    public Review putLikeToReview(Integer reviewId, Integer userId) {
        userService.get(userId);
        Review review = getReviewById(reviewId);
        UserReactionToReview userReaction = userReactionToReviewRepository.getReaction(userId, reviewId);

        if (userReaction == null) {
            userReaction = UserReactionToReview.builder()
                    .userId(userId)
                    .reviewId(reviewId)
                    .reaction(ReactionTypes.LIKE.toString())
                    .build();
            userReactionToReviewRepository.addNewReaction(userReaction);
        } else if (Objects.requireNonNull(userReaction).getReaction().equals(ReactionTypes.LIKE.toString())) {
            final String errorMessage = String.format("Попытка повторной установки лайка пользователем " +
                    "с id=%d на отзыв с id=%d.", userId, reviewId);
            log.warn(errorMessage);
            throw new DataDuplicationException(errorMessage);
        } else {
            userReaction.setReaction(ReactionTypes.LIKE.toString());
            userReactionToReviewRepository.updateReaction(userReaction);
            review.setUseful(review.getUseful() + 1);
        }

        review.setUseful(review.getUseful() + 1);
        log.info("Пользователь id={} поставил лайк отзыву id={}", userId, reviewId);
        return reviewRepository.updateReview(review);
    }

    @Override
    public Review putDislikeToReview(Integer reviewId, Integer userId) {
        userService.get(userId);
        Review review = getReviewById(reviewId);
        UserReactionToReview userReaction = userReactionToReviewRepository.getReaction(userId, reviewId);

        if (userReaction == null) {
            userReaction = UserReactionToReview.builder()
                    .userId(userId)
                    .reviewId(reviewId)
                    .reaction(ReactionTypes.DISLIKE.toString())
                    .build();
            userReactionToReviewRepository.addNewReaction(userReaction);
        } else if (Objects.requireNonNull(userReaction).getReaction().equals(ReactionTypes.DISLIKE.toString())) {
            final String errorMessage = String.format("Попытка повторной установки дизлайка пользователем " +
                    "с id=%d на отзыв с id=%d.", userId, reviewId);
            log.warn(errorMessage);
            throw new DataDuplicationException(errorMessage);
        } else {
            userReaction.setReaction(ReactionTypes.DISLIKE.toString());
            userReactionToReviewRepository.updateReaction(userReaction);
            review.setUseful(review.getUseful() - 1);
        }

        review.setUseful(review.getUseful() - 1);
        log.info("Пользователь id={} поставил дизлайк отзыву id={}", userId, reviewId);
        return reviewRepository.updateReview(review);
    }

    @Override
    public Review deleteLikeFromReview(Integer reviewId, Integer userId) {
        userService.get(userId);
        Review review = getReviewById(reviewId);
        UserReactionToReview userReaction = userReactionToReviewRepository.getReaction(userId, reviewId);

        if (userReaction == null) {
            final String errorMessage = String.format("Реакция пользователя с id=%d на отзыв с id=%d не найдена.",
                    userId, reviewId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        if (Objects.requireNonNull(userReaction).getReaction().equals(ReactionTypes.LIKE.toString())) {
            review.setUseful(review.getUseful() - 1);
        } else {
            review.setUseful(review.getUseful() + 1);
        }

        userReactionToReviewRepository.deleteReaction(userReaction);
        log.info("Пользователь id={} удалил лайк на отзыв id={}", userId, reviewId);
        return reviewRepository.updateReview(review);
    }

    @Override
    public Review deleteDislikeFromReview(Integer reviewId, Integer userId) {
        userService.get(userId);
        Review review = getReviewById(reviewId);
        UserReactionToReview userReaction = userReactionToReviewRepository.getReaction(userId, reviewId);

        if (userReaction == null) {
            final String errorMessage = String.format("Реакция пользователя с id=%d на отзыв с id=%d не найдена.",
                    userId, reviewId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        if (Objects.requireNonNull(userReaction).getReaction().equals(ReactionTypes.LIKE.toString())) {
            final String errorMessage = String.format("Дизлайк пользователя с id=%d на отзыв с id=%d не найден.",
                    userId, reviewId);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        userReactionToReviewRepository.deleteReaction(userReaction);
        review.setUseful(review.getUseful() + 1);
        log.info("Пользователь id={} удалил дизлайк на отзыв id={}", userId, reviewId);
        return reviewRepository.updateReview(review);
    }

}
