package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final List<User> users = new ArrayList<>();
    private int userId = 1;

    @PostMapping
    public User createUser(@RequestBody User user) {
        validateUser(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin()); // имя равно логину, когда имя не задано
        }
        user.setId(userId++);
        users.add(user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("Запрос на изменение пользователя: {}", user);
        validateUser(user);
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                return user;
            }
        }
        throw new ValidationException("Пользователь с указанным ID не найден.");
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new ValidationException("Неправильный email.");
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" "))
            throw new ValidationException("Неправильный логин.");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Дата рождения не может быть в будущем.");
    }
}
