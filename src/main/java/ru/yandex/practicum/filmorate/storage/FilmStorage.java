package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int filmId);

    List<Film> getAllFilms();

    Film getFilmById(int id);

    boolean existsById(int id);
}

