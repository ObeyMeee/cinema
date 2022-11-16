package ua.com.andromeda.cinemaspringbootapp.verification;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import ua.com.andromeda.cinemaspringbootapp.model.User;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private transient User user;

    public OnRegistrationCompleteEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }
}