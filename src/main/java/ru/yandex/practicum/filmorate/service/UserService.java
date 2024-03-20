package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        validateUser(user);
        userStorage.addUser(user);
        return user;
    }

    public User updateUser(User user) {
        if (!userStorage.existsById(user.getId())) {
            throw new ResourceNotFoundException("Пользователь с ID=" + user.getId() + " не найден.");
        }
        validateUser(user);
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new ValidationException("Пользователь с указанным ID не найден.");
        }
        return user;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        validateUserExists(userId);
        validateUserExists(friendId);
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public void removeFriend(int userId, int friendId) {
        validateUserExists(userId);
        validateUserExists(friendId);
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public Collection<User> getFriends(int id) {
        User user = getUserById(id);
        if (user == null) {
            throw new ValidationException("Пользователь с указанным ID не найден.");
        }
        Set<Integer> friendIds = user.getFriends();
        Set<User> friends = new HashSet<>();
        for (Integer friendId : friendIds) {
            User friend = getUserById(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        validateUserExists(userId);
        validateUserExists(otherUserId);
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);
        Set<Integer> commonFriendIds = new HashSet<>(user.getFriends());
        commonFriendIds.retainAll(otherUser.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (Integer friendId : commonFriendIds) {
            commonFriends.add(getUserById(friendId));
        }
        return commonFriends;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@"))
            throw new ValidationException("Неправильный email.");
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" "))
            throw new ValidationException("Неправильный логин.");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Дата рождения не может быть в будущем.");
    }

    private void validateUserExists(int userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new ValidationException("Пользователь с ID=" + userId + " не найден.");


        }

    }

}
