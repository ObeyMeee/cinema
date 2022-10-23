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
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.repository.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User is not founded"));
    }

    @Transactional
    public void save(User user) {
        String encodedPassword = getEncodedPassword(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
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
    public void delete(String id) {
        userRepository.deleteById(id);
    }
}
