package ua.com.andromeda.cinemaspringbootapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurityConfig(UserService userService, PasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/users/new").permitAll()
                    .antMatchers("/tickets/new").authenticated()
                    .antMatchers(HttpMethod.GET, "/users/**").authenticated()
                    .antMatchers("/users").hasAnyRole("ADMIN", "SUPER_ADMIN", "OWNER")
                    .antMatchers(HttpMethod.DELETE, "/users/**").hasAnyRole("ADMIN", "SUPER_ADMIN", "OWNER")
                    .anyRequest().permitAll()
                .and()
                    .formLogin()
                    .defaultSuccessUrl("/home")
                .and()
                    .logout()
                    .logoutSuccessUrl("/home")
                    .permitAll()
                .and()
                    .rememberMe()
                .and()
                    .csrf().disable();
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }
}
