package ua.com.andromeda.cinemaspringbootapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.andromeda.cinemaspringbootapp.repository.SessionRepository;

@SpringBootTest(args = {
        "--DATABASE_URL=jdbc:postgresql://localhost:5432/cinema",
        "--DATABASE_USERNAME=postgres",
        "--DATABASE_PASSWORD=1337",
        "--EMAIL=andrrromeda88@gmail.com",
        "--EMAIL_PASSWORD=zpvfssjoeklutmud",
        "--APPLICATION_URL=http://localhost:8080"
})
class SessionServiceTest {

    SessionService target;
    @MockBean
    SessionRepository sessionRepository;

    @Autowired
    public SessionServiceTest(SessionService sessionService) {
        this.target = sessionService;
    }

    @Test
    void hasAccess() {
    }

    @Test
    void findById() {
    }

    @Test
    void findUniqueSessions() {
    }

    @Test
    void findAllByMovieDetailsId() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findDistinctByMovieDetailsId() {
    }

    @Test
    void save() {
    }


    @Test
    void saveAll() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteAllByMovieDetailsId() {
    }
}