package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.repository.DirectorRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;

    @Override
    public Director create(Director director) {
        return directorRepository.create(director);
    }

    @Override
    public Director update(Director director) {
        return directorRepository.update(director);
    }

    @Override
    public Director getById(Integer id) {
        return directorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Режиссер c id " + id + " не найден."));
    }

    @Override
    public void removeById(Integer id) {
        directorRepository.deleteById(id);
    }

    @Override
    public Collection<Director> getAll() {
        return directorRepository.findAll();
    }

    @Override
    public boolean isDirectorExists(Integer id) {
        return directorRepository.isExists(id);
    }
}