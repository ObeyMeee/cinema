package ua.com.andromeda.cinemaspringbootapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.model.VerificationToken;
import ua.com.andromeda.cinemaspringbootapp.repository.VerificationTokenRepository;

import static org.junit.jupiter.api.Assertions.*;
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
class TokenServiceTest {
    final TokenService target;
    @MockBean
    VerificationTokenRepository tokenRepository;

    @Autowired
    TokenServiceTest(TokenService target) {
        this.target = target;
    }

    @Test
    void getVerificationToken_found() {
        when(tokenRepository.findByToken(anyString())).thenReturn(getSimpleToken());
        VerificationToken expected = getSimpleToken();
        VerificationToken actual = target.getVerificationToken("");
        assertEquals(expected.getToken(), actual.getToken());
    }

    @Test
    void getVerificationToken_notFound() {
        when(tokenRepository.findByToken(anyString())).thenReturn(null);
        VerificationToken actual = target.getVerificationToken("");
        assertNull(actual);
    }

    private VerificationToken getSimpleToken() {
        return new VerificationToken("founded", new User());
    }


    @Test
    void createVerificationToken_tokenNull() {
        assertThrows(NullPointerException.class, () -> target.createVerificationToken(new User(), null));
    }

    @Test
    void createVerificationToken_userNull() {
        target.createVerificationToken(null, "");
        verify(tokenRepository, times(1)).save(any(VerificationToken.class));
    }

    @Test
    void createVerificationToken() {
        target.createVerificationToken(new User(), "");
        verify(tokenRepository, times(1)).save(any(VerificationToken.class));
    }

}