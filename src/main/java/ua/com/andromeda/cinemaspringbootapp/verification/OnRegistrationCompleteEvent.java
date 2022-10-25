package ua.com.andromeda.cinemaspringbootapp.verification;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import ua.com.andromeda.cinemaspringbootapp.model.User;

import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private transient User user;

    public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }
}