package ua.com.andromeda.cinemaspringbootapp.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.model.VerificationToken;
import ua.com.andromeda.cinemaspringbootapp.repository.VerificationTokenRepository;

@Service
public class TokenService {
    private final VerificationTokenRepository tokenRepository;

    public TokenService(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }

    public void createVerificationToken(User user, @NonNull String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }
}
