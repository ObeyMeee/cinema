package ua.com.andromeda.cinemaspringbootapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.andromeda.cinemaspringbootapp.model.MovieDetails;
import ua.com.andromeda.cinemaspringbootapp.repository.MovieDetailsRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(args = {
        "--DATABASE_URL=jdbc:postgresql://localhost:5432/cinema",
        "--DATABASE_USERNAME=postgres",
        "--DATABASE_PASSWORD=1337",
        "--EMAIL=andrrromeda88@gmail.com",
        "--EMAIL_PASSWORD=zpvfssjoeklutmud",
        "--APPLICATION_URL=http://localhost:8080"
})
class MovieDetailsServiceTest {
    final MovieDetailsService target;
    @MockBean
    MovieDetailsRepository movieDetailsRepository;

    @Autowired
    MovieDetailsServiceTest(MovieDetailsService movieDetailsService) {
        this.target = movieDetailsService;
    }


    @Test
    void getMovieDetailsById_exist() {
        MovieDetails expected = buildSimpleMovieDetails();
        when(movieDetailsRepository.findById("12345")).thenReturn(Optional.of(expected));
        MovieDetails actual = target.getMovieDetailsById("12345");
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void getMovieDetailsById_notExits() {
        when(movieDetailsRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> target.getMovieDetailsById("1"),
                "Cannot find movie details with id 1");
    }

    private MovieDetails buildSimpleMovieDetails() {
        MovieDetails movieDetails = new MovieDetails();
        movieDetails.setId("12345");
        return movieDetails;
    }

    @Test
    void save() {
        MovieDetails movieDetails = buildSimpleMovieDetails();
        movieDetailsRepository.save(movieDetails);
        verify(movieDetailsRepository, times(1)).save(movieDetails);
    }
}