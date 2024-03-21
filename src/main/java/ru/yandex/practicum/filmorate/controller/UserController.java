package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Новый пользователь {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        log.info("Обновление данных пользователя {}", updatedUser);
        return updatedUser;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        log.info("Получение пользователя с id={}", id);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }

    @GetMapping()
    public Collection<User> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователь {} добавлен в друзья пользователю {}", friendId, id);
    }


    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.removeFriend(userId, friendId);
        log.info("Пользователь {} удален из друзей пользователя {}", friendId, userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получение общих друзей пользователей {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
