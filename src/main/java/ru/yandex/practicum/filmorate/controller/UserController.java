package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userServiceImpl.getUsers();
    }

    @PostMapping
    public User create(@RequestBody @Valid final User user) {
        return userServiceImpl.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid final User user) {
        return userServiceImpl.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(
            @PathVariable(name = "id") @Positive final Integer userId,
            @PathVariable @Positive final Integer friendId
    ) {
        userServiceImpl.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(
            @PathVariable(name = "id") @Positive final Integer userId,
            @PathVariable @Positive final Integer friendId
    ) {
        userServiceImpl.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable(name = "id") @Positive final Integer userId) {
        return userServiceImpl.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(
            @PathVariable(name = "id") @Positive final Integer firstUserId,
            @PathVariable(name = "otherId") @Positive final Integer secondUserId
    ) {
        return userServiceImpl.getCommonFriends(firstUserId, secondUserId);
    }
}
