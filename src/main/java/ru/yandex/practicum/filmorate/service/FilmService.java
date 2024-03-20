package ru.yandex.practicum.filmorate.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        return filmStorage.addFilm(film);
    }


    public Film updateFilm(Film film) {
        if (!filmStorage.existsById(film.getId())) {
            throw new ResourceNotFoundException("Фильм с ID=" + film.getId() + " не найден.");
        }
        validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new ValidationException("Фильм с ID " + filmId + " не найден");
        }
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
    }


    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.getLikes().remove(userId);
    }

    public List<Film> getMostPopularFilms(int count) {
        List<Film> films = new ArrayList<>(filmStorage.getAllFilms());
        Collections.sort(films, new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return Integer.compare(o2.getLikes().size(), o1.getLikes().size());
            }
        });
        if (films.size() > count) {
            return films.subList(0, count);
        } else {
            return films;
        }
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("название не может быть пустым.");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание больше 200 символов.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза раньше 28 декабря 1895 года.");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной.");

        }
    }
}
