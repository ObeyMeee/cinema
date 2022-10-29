package ua.com.andromeda.cinemaspringbootapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(args = {
        "--DATABASE_URL=jdbc:postgresql://localhost:5432/cinema",
        "--DATABASE_USERNAME=postgres",
        "--DATABASE_PASSWORD=1337",
        "--EMAIL=andrrromeda88@gmail.com",
        "--EMAIL_PASSWORD=zpvfssjoeklutmud",
        "--APPLICATION_URL=http://localhost:8080"
})
class UserServiceTest {
    final UserService target;
    @MockBean
    UserRepository userRepository;
    @MockBean
    Authentication authentication;

    @Autowired
    UserServiceTest(UserService userService) {
        this.target = userService;
    }

    private User getSimpleUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("emai@email.com");
        user.setLogin("login");
        user.setPassword("password");
        user.setEnabled(true);
        Set<Role> roles = new HashSet<>(List.of(new Role(), new Role()));
        user.setRoles(roles);
        return user;
    }

    @Test
    void hasAccess_sameUser() {
        String login = "passedLogin";
        when(authentication.getName()).thenReturn(login);
        boolean actual = target.hasAccess(login, authentication);
        assertTrue(actual);
    }

    @Test
    void hasAccess_authenticatedUserRolesCountMoreThanFounded() {
        String login = "passedLogin";
        when(authentication.getName()).thenReturn("notTheSameUser");
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(getSimpleUser()));

        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_CUSTOMER"),
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")
        );
        doReturn(authorities).when(authentication).getAuthorities();
        boolean actual = target.hasAccess(login, authentication);
        assertTrue(actual);
    }

    @Test
    void hasAccess_foundedUserHasMoreRolesCountThanAuthenticated() {
        String login = "passedLogin";
        when(authentication.getName()).thenReturn("notTheSameUser");
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(getSimpleUser()));

        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        doReturn(authorities).when(authentication).getAuthorities();
        boolean actual = target.hasAccess(login, authentication);
        assertFalse(actual);
    }

    @Test
    void hasAccess_foundedUserHasSameRolesCountThanAuthenticated() {
        String login = "passedLogin";
        when(authentication.getName()).thenReturn("notTheSameUser");
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(getSimpleUser()));

        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"),
                new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        doReturn(authorities).when(authentication).getAuthorities();
        boolean actual = target.hasAccess(login, authentication);
        assertFalse(actual);
    }


    @Test
    void findById_found() {
        User expected = getSimpleUser();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(expected));
        User actual = target.findById("123");
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void findById_notFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> target.findById("123"),
                "User is not founded");
    }


    @Test
    void findByLogin_found() {
        User expected = getSimpleUser();
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(expected));
        User actual = target.findByLogin("login");
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void findByLogin_notFound() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> target.findByLogin("login"),
                "Cannot find user with login ==> login");
    }

    @Test
    void save() {
        User user = getSimpleUser();
        String password = user.getPassword();
        target.save(user);
        verify(userRepository, times(1)).save(any());
        assertNotEquals(password, user.getPassword());
    }

    @Test
    void update() {
        User user = getSimpleUser();
        target.update(user);
        verify(userRepository, times(1)).save(any());
    }
}