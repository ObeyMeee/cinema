package ua.com.andromeda.cinemaspringbootapp.validator;

import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.model.VerificationToken;

import java.util.Calendar;

@Component
public class VerificationTokenValidator {
    public boolean validate(VerificationToken token) {

        if (token == null) {
            return false;
        }
        return isNonExpired(token);
    }

    private boolean isNonExpired(VerificationToken verificationToken) {
        Calendar calendar = Calendar.getInstance();
        long expirationTime = verificationToken.getExpiryDate().getTime();
        long now = calendar.getTime().getTime();
        return now - expirationTime > 0;
    }
}
