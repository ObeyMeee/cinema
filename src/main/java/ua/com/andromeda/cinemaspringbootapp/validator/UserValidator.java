package ua.com.andromeda.cinemaspringbootapp.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.repository.UserRepository;

import java.util.Optional;

@Component
public class UserValidator {
    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String validateRegisteringUser(User user) {
        String email = user.getEmail();
        if (emailExists(email)) {
            return "User with email " + email + " already exists";
        }
        String login = user.getLogin();
        if (loginExists(login)) {
            return "User with login " + login + " already exists";
        }
        return "";
    }

    private boolean emailExists(String email) {
        Optional<User> optionalUserByEmail = userRepository.findByEmail(email);
        return optionalUserByEmail.isPresent();
    }

    private boolean loginExists(String login) {
        Optional<User> optionalUserByLogin = userRepository.findByLogin(login);
        return optionalUserByLogin.isPresent();
    }

    public String validateUpdatedUser(User userToBeUpdated) {
        User founded = userRepository.findById(userToBeUpdated.getId())
                .orElseThrow(() -> new IllegalArgumentException("User is not founded"));

        String updatedEmail = userToBeUpdated.getEmail();
        if (!founded.getEmail().equals(updatedEmail) && emailExists(updatedEmail)) {
            return "User with email " + updatedEmail + " already exists";
        }
        String updatedLogin = userToBeUpdated.getLogin();
        if (!founded.getLogin().equals(updatedLogin) && loginExists(updatedLogin)) {
            return "User with login " + updatedLogin + " already exists";
        }
        return "";
    }
}
