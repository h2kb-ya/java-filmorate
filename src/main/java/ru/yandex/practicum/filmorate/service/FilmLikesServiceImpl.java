package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.OperationTypes;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmLikesRepository;

@Service
@RequiredArgsConstructor
public class FilmLikesServiceImpl implements FilmLikesService {

    private final FilmLikesRepository filmLikesRepository;
    private final EventService eventService;

    @Override
    public void like(Film film, User user) {
        filmLikesRepository.like(film, user);
        eventService.addEvent(user.getId(), EventTypes.LIKE, OperationTypes.ADD, film.getId());
    }

    @Override
    public void dislike(Film film, User user) {
        filmLikesRepository.dislike(film, user);
        eventService.addEvent(user.getId(), EventTypes.LIKE, OperationTypes.REMOVE, film.getId());
    }
}
