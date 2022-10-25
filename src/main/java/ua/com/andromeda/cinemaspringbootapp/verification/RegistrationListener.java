package ua.com.andromeda.cinemaspringbootapp.verification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;
import ua.com.andromeda.cinemaspringbootapp.utils.mail.EmailSenderService;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final UserService service;

    private final EmailSenderService emailSenderService;

    @Autowired
    public RegistrationListener(UserService service, EmailSenderService emailSenderService) {
        this.service = service;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        String confirmationUrl = event.getAppUrl() + "http://localhost:8080/users/registrationConfirm?token=" + token;
        service.createVerificationToken(user, token);
        emailSenderService.sendVerificationEmail(user,confirmationUrl);
    }
}