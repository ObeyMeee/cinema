package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.model.VerificationToken;
import ua.com.andromeda.cinemaspringbootapp.repository.UserRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.VerificationTokenRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, VerificationTokenRepository tokenRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean hasAccess(String passedLogin, Authentication authentication) {
        boolean isSameUser = passedLogin.equals(authentication.getName());
        if (isSameUser) {
            return true;
        }

        int size = findByLogin(passedLogin).getRoles().size();
        return authentication.getAuthorities().size() > size;
    }

    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User is not founded"));
    }

    @Transactional
    public void save(User user) {
        String encodedPassword = getEncodedPassword(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Transactional
    public void update(User user) {
        userRepository.save(user);
    }

    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = findByLogin(login);

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(), user.getPassword(), user.isEnabled(), true,
                true, true, mapRolesToGrantedAuthorities(user.getRoles()));
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

    public List<User> findAllWhereUsersHaveSameOrLessRoles(String login) {
        int size = getAmountUserRolesByLogin(login);
        return userRepository.findAllWhereUsersHaveSameOrLessRolesCount(size);
    }

    private int getAmountUserRolesByLogin(String login) {
        User currentUser = findByLogin(login);
        Set<Role> roles = currentUser.getRoles();
        return roles.size();
    }

    @Transactional
    public void delete(User user) {
        Set<Ticket> tickets = user.getTickets();
        tickets.forEach(ticket -> ticket.setUser(null));
        userRepository.delete(user);
    }
}
