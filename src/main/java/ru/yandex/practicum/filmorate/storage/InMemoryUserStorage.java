package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @Override
    public User addUser(User user) {
        user.setId(userId++);
        users.put(user.getId(), user);
        return user;
    }


    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с ID=" + user.getId() + " не найден.");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        users.remove(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }


    @Override
    public User getUserById(int id) {
        return users.get(id);
    }
    @Override
    public boolean existsById(int id) {
        return users.containsKey(id);
    }
}