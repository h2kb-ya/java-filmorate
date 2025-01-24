package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable(name = "id") @Positive Integer id) {
        return userService.get(id);
    }

    @PostMapping
    public User create(@RequestBody @Valid final User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid final User user) {
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") @Positive final Integer id) {
        userService.deleteById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable(name = "id") @Positive final Integer userId,
            @PathVariable final Integer friendId
    ) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(
            @PathVariable(name = "id") @Positive final Integer userId,
            @PathVariable final Integer friendId
    ) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable(name = "id") @Positive final Integer userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable(name = "id") @Positive final Integer firstUserId,
            @PathVariable(name = "otherId") final Integer secondUserId
    ) {
        return userService.getCommonFriends(firstUserId, secondUserId);
    }

    @GetMapping("/{id}/feed")
    public Collection<EventDto> getUserFeed(@PathVariable(name = "id") @Positive final Integer userId) {
        return userService.getUserFeed(userId);
    }

    @GetMapping("{id}/recommendations")
    public Collection<Film> getRecommendations(@PathVariable(name = "id") @Positive final Integer id) {
        return userService.getRecommendations(id);
    }
}