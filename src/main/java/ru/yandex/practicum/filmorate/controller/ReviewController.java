package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.util.FilmorateConstants.DEFAULT_COUNT_VALUE_FOR_GETTING_REVIEWS;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review addNewReview(@RequestBody @Valid Review newReview) {
        return reviewService.addNewReview(newReview);
    }

    @PutMapping
    public Review updateCurrentReview(@RequestBody @Valid Review currentReview) {
        return reviewService.updateCurrentReview(currentReview);
    }

    @DeleteMapping("/{id}")
    public Review deleteCurrentReview(@PathVariable @Positive Integer id) {
        return reviewService.deleteCurrentReview(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable @Positive Integer id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping
    public Collection<Review> getReviewsListByFilmId(
            @RequestParam(required = false) Integer filmId,
            @RequestParam(defaultValue = DEFAULT_COUNT_VALUE_FOR_GETTING_REVIEWS) @Positive Integer count
    ) {
        return reviewService.getReviewsList(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Review putLikeToReview(
            @PathVariable @Positive Integer id,
            @PathVariable Integer userId
    ) {
        return reviewService.putLikeToReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Review putDislikeToReview(
            @PathVariable @Positive Integer id,
            @PathVariable Integer userId
    ) {
        return reviewService.putDislikeToReview(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Review deleteLikeFromReview(
            @PathVariable @Positive Integer id,
            @PathVariable Integer userId
    ) {
        return reviewService.deleteLikeFromReview(id, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public Review deleteDislikeFromReview(
            @PathVariable @Positive Integer id,
            @PathVariable Integer userId
    ) {
        return reviewService.deleteDislikeFromReview(id, userId);
    }
}