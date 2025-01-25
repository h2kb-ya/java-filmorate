package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.util.FilmorateConstants.DEFAULT_COUNT_VALUE_FOR_GETTING_REVIEWS;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ReviewDto addNewReview(@RequestBody @Valid Review newReview) {
        return ReviewMapper.toDto(reviewService.addNewReview(newReview));
    }

    @PutMapping
    public ReviewDto updateCurrentReview(@RequestBody @Valid Review currentReview) {
        return ReviewMapper.toDto(reviewService.updateCurrentReview(currentReview));
    }

    @DeleteMapping("/{id}")
    public ReviewDto deleteCurrentReview(@PathVariable @Positive Integer id) {
        return ReviewMapper.toDto(reviewService.deleteCurrentReview(id));
    }

    @GetMapping("/{id}")
    public ReviewDto getReviewById(@PathVariable @Positive Integer id) {
        return ReviewMapper.toDto(reviewService.getReviewById(id));
    }

    @GetMapping
    public Collection<ReviewDto> getReviewsListByFilmId(
            @RequestParam(required = false) Integer filmId,
            @RequestParam(defaultValue = DEFAULT_COUNT_VALUE_FOR_GETTING_REVIEWS) @Positive Integer count
    ) {
        return reviewService.getReviewsList(filmId, count).stream()
                .map(ReviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/like/{userId}")
    public ReviewDto putLikeToReview(
            @PathVariable @Positive Integer id,
            @PathVariable Integer userId
    ) {
        return ReviewMapper.toDto(reviewService.putLikeToReview(id, userId));
    }

    @PutMapping("/{id}/dislike/{userId}")
    public ReviewDto putDislikeToReview(
            @PathVariable @Positive Integer id,
            @PathVariable Integer userId
    ) {
        return ReviewMapper.toDto(reviewService.putDislikeToReview(id, userId));
    }

    @DeleteMapping("{id}/like/{userId}")
    public ReviewDto deleteLikeFromReview(
            @PathVariable @Positive Integer id,
            @PathVariable Integer userId
    ) {
        return ReviewMapper.toDto(reviewService.deleteLikeFromReview(id, userId));
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public ReviewDto deleteDislikeFromReview(
            @PathVariable @Positive Integer id,
            @PathVariable Integer userId
    ) {
        return ReviewMapper.toDto(reviewService.deleteDislikeFromReview(id, userId));
    }
}
