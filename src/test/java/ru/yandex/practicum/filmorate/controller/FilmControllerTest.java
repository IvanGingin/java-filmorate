package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    private FilmController filmController;


    @Test
    public void addFilm_EmptyNameThrowsValidationException() {
        Film film = new Film();
        film.setDescription("Тестовое описание");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addFilm_LongDescriptionThrowsValidationException() {
        Film film = new Film();
        film.setName("Тестовое название");
        film.setDescription("Это очень длинное описание".repeat(10));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addFilm_ReleaseDateBefore1895ThrowsValidationException() {
        Film film = new Film();
        film.setName("Тестовое название");
        film.setDescription("Тестовое описание");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(120);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    public void addFilm_NegativeDurationThrowsValidationException() {
        Film film = new Film();
        film.setName("Тестовое название");
        film.setDescription("Тестовое описание");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-120);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }
}

