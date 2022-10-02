package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Transactional
    public boolean save(User user) {
        if (emailExists(user)) {
            return false;
        }
        if (loginExists(user)) {
            return false;
        }

        encodePassword(user);
        userRepository.save(user);
        return true;
    }

    private boolean emailExists(User user) {
        String email = user.getEmail();
        Optional<User> optionalUserByEmail = userRepository.findByEmail(email);
        if (optionalUserByEmail.isPresent()) {
            System.out.println("There is user with email = " + email);
            return true;
        }
        return false;
    }

    private boolean loginExists(User user) {
        String login = user.getLogin();
        Optional<User> optionalUserByLogin = findByLogin(login);
        if (optionalUserByLogin.isPresent()) {
            System.out.println("There is user with login = " + login);
            return true;
        }
        return false;
    }

    private void encodePassword(User user) {
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with login ==> " + login));

        return new org.springframework.security.core.userdetails.User
                (user.getLogin(), user.getPassword(), mapRolesToGrantedAuthorities(user.getRoles()));
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    private Collection<? extends GrantedAuthority> mapRolesToGrantedAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(String id) {
        userRepository.deleteById(id);
    }
}
