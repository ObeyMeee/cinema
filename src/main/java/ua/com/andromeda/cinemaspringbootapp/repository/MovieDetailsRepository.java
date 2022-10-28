package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.andromeda.cinemaspringbootapp.model.Country;
import ua.com.andromeda.cinemaspringbootapp.model.MovieDetails;

import java.util.Optional;

@Repository
public interface MovieDetailsRepository extends JpaRepository<MovieDetails, String> {
    Optional<MovieDetails> findByDirectorAndProductionYearAndCountry(String director,
                                                                     int productionYear,
                                                                     Country country);
}

