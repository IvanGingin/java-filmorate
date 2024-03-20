package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    private UserController userController;


    @Test
    public void createUser_EmptyEmailThrowsValidationException() {
        User user = new User();
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUser_InvalidEmailThrowsValidationException() {
        User user = new User();
        user.setEmail("неверныйEmail");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUser_EmptyLoginThrowsValidationException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUser_LoginWithSpacesThrowsValidationException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("логин с пробелами");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUser_FutureBirthdayThrowsValidationException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }
}
