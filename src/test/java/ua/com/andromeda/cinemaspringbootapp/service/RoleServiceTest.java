package ua.com.andromeda.cinemaspringbootapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.repository.RoleRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(args = {
        "--DATABASE_URL=jdbc:postgresql://localhost:5432/cinema",
        "--DATABASE_USERNAME=postgres",
        "--DATABASE_PASSWORD=1337",
        "--EMAIL=andrrromeda88@gmail.com",
        "--EMAIL_PASSWORD=zpvfssjoeklutmud",
        "--APPLICATION_URL=http://localhost:8080"
})
class RoleServiceTest {
    final RoleService target;
    @MockBean
    RoleRepository roleRepository;

    @Autowired
    RoleServiceTest(RoleService roleService) {
        this.target = roleService;
    }

    @Test
    void findAllExceptOwner() {
        when(roleRepository.findAllByNameIsNotLike(anyString())).thenReturn(getSimpleRoles());
        int expected = 3;
        int actualSize = target.findAllExceptOwner().size();
        assertEquals(expected, actualSize);
    }

    public List<Role> getSimpleRoles() {
        return List.of(new Role(), new Role(), new Role());
    }

}