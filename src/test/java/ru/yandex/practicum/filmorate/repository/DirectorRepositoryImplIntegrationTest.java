package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru/yandex/practicum/filmorate/*")
public class DirectorRepositoryImplIntegrationTest {

    private final DirectorRepositoryImpl directorRepository;

    private static Director getTestDirector1() {
        return new Director(1, "Кристофер Нолан");
    }

    private static Director getTestDirector2() {
        return new Director(2, "Лилли Вачовски");
    }

    private static Director getTestDirector3() {
        return new Director(3, "Лана Вачовски");
    }

    private static Director getTestDirector4() {
        return new Director(4, "Фрэнк Дарабонт");
    }

    @Test
    public void findAll_directorsExist_returnDirectors() {
        Collection<Director> directors = directorRepository.findAll();
        assertThat(directors)
                .isNotEmpty()
                .hasSize(4)
                .allMatch(Objects::nonNull)
                .contains(getTestDirector1(), getTestDirector2(), getTestDirector3(), getTestDirector4());
    }

    @Test
    public void findById_directorExist_returnOptionalDirector() {
        Optional<Director> director = directorRepository.findById(getTestDirector1().getId());
        System.out.println(director);
        assertThat(director).isNotEmpty();
        assertThat(director.get()).isEqualTo(getTestDirector1());
    }

    @Test
    public void findById_directorDoesNotExist_returnOptionalEmpty() {
        Optional<Director> director = directorRepository.findById(999);
        assertThat(director).isEmpty();
    }

    @Test
    public void create_happyPath_returnCreatedDirector() {
        Director director = new Director("Гай Ричи");

        Integer createdDirectorId = directorRepository.create(director).getId();
        director.setId(createdDirectorId);

        assertThat(Objects.equals(directorRepository.findById(director.getId()).orElse(null), director)).isTrue();
    }

    @Test
    public void update_happyPath_returnUpdatedDirector() {
        Director director = getTestDirector1();
        director.setName("Updated Director Name");

        directorRepository.update(director);

        assertThat(Objects.equals(directorRepository.findById(director.getId()).orElse(null), director)).isTrue();
    }

    @Test
    public void delete_directorExists_directorDeleted() {
        Optional<Director> existedDirector = directorRepository.findById(getTestDirector1().getId());
        assertThat(existedDirector).isNotEmpty();

        directorRepository.deleteById(getTestDirector1().getId());
        existedDirector = directorRepository.findById(getTestDirector1().getId());
        assertThat(existedDirector).isEmpty();
    }

    @Test
    public void deleteAll_directorExist_directorsDeleted() {
        Collection<Director> directors = directorRepository.findAll();
        assertThat(directors.size()).isEqualTo(4);

        directorRepository.deleteAll();
        directors = directorRepository.findAll();
        assertThat(directors).isEmpty();
    }
}