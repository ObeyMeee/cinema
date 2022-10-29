package ua.com.andromeda.cinemaspringbootapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.andromeda.cinemaspringbootapp.model.Genre;
import ua.com.andromeda.cinemaspringbootapp.repository.GenreRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(args = {
        "--DATABASE_URL=jdbc:postgresql://localhost:5432/cinema",
        "--DATABASE_USERNAME=postgres",
        "--DATABASE_PASSWORD=1337",
        "--EMAIL=andrrromeda88@gmail.com",
        "--EMAIL_PASSWORD=zpvfssjoeklutmud",
        "--APPLICATION_URL=http://localhost:8080"
})
class GenreServiceTest {
    final GenreService target;
    @MockBean
    GenreRepository genreRepository;

    @Autowired
    GenreServiceTest (GenreService genreService) {
        this.target = genreService;
    }

    @Test
    void findAll() {
        when(genreRepository.findAll())
                .thenReturn(getSimpleGenreList());
        int expected = 4;
        int actual = target.findAll().size();
        assertEquals(expected, actual);
    }

    private List<Genre> getSimpleGenreList() {
        return List.of(buildSimpleGenre("Fantasy"),
                buildSimpleGenre("Action"),
                buildSimpleGenre("Adventure"),
                buildSimpleGenre("Drama"));
    }

    private Genre buildSimpleGenre(String name) {
        Genre genre = new Genre();
        genre.setId(UUID.randomUUID().toString());
        genre.setName(name);
        return genre;
    }
}