package ua.com.andromeda.cinemaspringbootapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("ua.com.andromeda.cinemaspringbootapp.repository")
@EntityScan("ua.com.andromeda.cinemaspringbootapp.model")
public class CinemaSpringBootAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaSpringBootAppApplication.class, args);
    }

}
