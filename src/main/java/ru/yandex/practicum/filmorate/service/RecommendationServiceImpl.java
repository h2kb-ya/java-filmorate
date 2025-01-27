package ru.yandex.practicum.filmorate.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmLikesRepository;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    @Qualifier("filmRepositoryImpl")
    private final FilmRepository filmRepository;
    @Qualifier("userRepositoryImpl")
    private final UserRepository userRepository;
    private final FilmLikesRepository filmLikesRepository;

    @Override
    public List<Film> getRecommendations(final Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id " + userId + " не найден."));
        List<User> allUsers = userRepository.findAll().stream().toList();
        User similarUser = findMostSimilarUser(user, allUsers);

        if (similarUser == null) {
            return List.of();
        }

        Set<Integer> commonFilmIds = getCommonFilms(user, similarUser);
        List<Integer> userLikedFilms = filmLikesRepository.getLikedFilmIds(similarUser.getId());

        return filmRepository.findFilmsByIds(userLikedFilms.stream()
                .filter(filmId -> !commonFilmIds.contains(filmId))
                .collect(Collectors.toSet())).stream().toList();
    }

    private User findMostSimilarUser(User targetUser, List<User> allUsers) {
        int maxCommonFilmsCount = 0;
        User mostSimilarUser = null;

        for (User user : allUsers) {
            if (!user.equals(targetUser)) {
                Set<Integer> commonFilms = getCommonFilms(targetUser, user);
                if (commonFilms.size() > maxCommonFilmsCount) {
                    maxCommonFilmsCount = commonFilms.size();
                    mostSimilarUser = user;
                }
            }
        }

        return mostSimilarUser;
    }

    private Set<Integer> getCommonFilms(User targetUser, User otherUser) {
        Set<Integer> targetUserLikedFilms = new HashSet<>(filmLikesRepository.getLikedFilmIds(targetUser.getId()));
        Set<Integer> otherUserLikedFilms = new HashSet<>(filmLikesRepository.getLikedFilmIds(otherUser.getId()));

        targetUserLikedFilms.retainAll(otherUserLikedFilms);
        return targetUserLikedFilms;
    }

}
