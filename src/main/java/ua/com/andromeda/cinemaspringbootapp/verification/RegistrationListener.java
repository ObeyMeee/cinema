package ua.com.andromeda.cinemaspringbootapp.verification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.TokenService;
import ua.com.andromeda.cinemaspringbootapp.utils.EmailSenderService;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final TokenService tokenService;

    private final EmailSenderService emailSenderService;

    @Autowired
    public RegistrationListener(TokenService tokenService, EmailSenderService emailSenderService) {
        this.tokenService = tokenService;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        String confirmationUrl = event.getAppUrl() + "/registration-confirm?token=" + token;
        tokenService.createVerificationToken(user, token);
        emailSenderService.sendVerificationEmail(user, confirmationUrl);
    }
}