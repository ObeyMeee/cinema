package ua.com.andromeda.cinemaspringbootapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(args = {
        "--DATABASE_URL=jdbc:postgresql://localhost:5432/cinema",
        "--DATABASE_USERNAME=postgres",
        "--DATABASE_PASSWORD=1337",
        "--EMAIL=andrrromeda88@gmail.com",
        "--EMAIL_PASSWORD=zpvfssjoeklutmud",
        "--APPLICATION_URL=http://localhost:8080"
})
class UserServiceTest {

    @Test
    void loadUserByUsername() {
    }

    @Test
    void hasAccess() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByLogin() {
    }

    @Test
    void findAllWhereUsersHaveSameOrLessRolesCount() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}