package ru.yandex.practicum.filmorate.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.OperationTypes;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.EventRepository;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Qualifier("userRepositoryImpl")
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @Override
    public Collection<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User get(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id " + userId + " не найден."));
    }

    @Override
    public User create(final User user) {
        return userRepository.create(user);
    }

    @Override
    public User update(final User user) {
        if (userExists(user.getId())) {
            userRepository.update(user);
        }

        return user;
    }

    @Override
    public void deleteById(Integer id) {
        if (userExists(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException("Пользователь с id - " + id + "не найден.");
        }
    }

    @Override
    public void addFriend(final Integer userId, final Integer friendId) {
        if (userExists(userId) && userExists(friendId)) {
            friendshipRepository.addFriend(userId, friendId);
            eventService.addEvent(userId, EventTypes.FRIEND, OperationTypes.ADD, friendId);
        }
    }

    @Override
    public void removeFriend(final Integer userId, final Integer friendId) {
        if (userExists(userId) && userExists(friendId)) {
            friendshipRepository.removeFriend(userId, friendId);
            eventService.addEvent(userId, EventTypes.FRIEND, OperationTypes.REMOVE, friendId);
        }
    }

    @Override
    public Collection<User> getFriends(final Integer userId) {
        userExists(userId);

        return friendshipRepository.getFriends(userId).stream()
                .map(this::get)
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Collection<User> getCommonFriends(final Integer firstUserId, final Integer secondUserId) {
        userExists(firstUserId);
        userExists(secondUserId);
        Collection<Integer> firstUserFriends = friendshipRepository.getFriends(firstUserId);

        Collection<Integer> secondUserFriends = friendshipRepository.getFriends(secondUserId);

        return firstUserFriends.stream()
                .filter(secondUserFriends::contains)
                .map(this::get)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean userExists(final Integer userId) {
        User user = get(userId);

        return user != null;
    }

    @Override
    public Collection<EventDto> getUserFeed(Integer userId) {
        if (userExists(userId)) {
            return eventRepository.getUserFeed(userId).stream()
                    .map(EventMapper::mapToEventDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Пользователь с id - " + userId + "не найден.");
        }
    }

}
