package ua.com.andromeda.cinemaspringbootapp;

import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class AuthenticationService {
    public boolean hasAccess(String userId, Principal principal) {
        return true;
    }
}
