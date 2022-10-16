package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User is not founded"));
    }

    @Transactional
    public void save(User user) {
        if (emailExists(user.getEmail()) || loginExists(user.getLogin())) {
            return;
        }
        encodePassword(user);
        userRepository.save(user);
    }

    private boolean emailExists(String email) {
        Optional<User> optionalUserByEmail = userRepository.findByEmail(email);
        if (optionalUserByEmail.isPresent()) {
            System.out.println("There is user with email = " + email);
            return true;
        }
        return false;
    }

    private boolean loginExists(String login) {
        Optional<User> optionalUserByLogin = userRepository.findByLogin(login);
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
        User user = findByLogin(login);
        return new org.springframework.security.core.userdetails.User
                (user.getLogin(), user.getPassword(), mapRolesToGrantedAuthorities(user.getRoles()));
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with login ==> " + login));
    }

    private Collection<? extends GrantedAuthority> mapRolesToGrantedAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    public Page<User> findAllWhereUsersHaveSameOrLessRoles(Principal principal, Pageable pageable) {
        String login = principal.getName();
        User currentUser = findByLogin(login);
        Set<Role> roles = currentUser.getRoles();
        return userRepository.findAllWhereUsersHaveSameOrLessRoles(roles.size(), pageable);
    }

    @Transactional
    public void delete(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void update(User user) {
        userRepository.save(user);
    }
}
