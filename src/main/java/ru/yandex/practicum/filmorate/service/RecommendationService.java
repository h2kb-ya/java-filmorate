package ru.yandex.practicum.filmorate.service;

import java.util.List;
import ru.yandex.practicum.filmorate.model.Film;

public interface RecommendationService {

    List<Film> getRecommendations(Integer userId);

}
