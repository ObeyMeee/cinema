package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.repository.CrudRepository;
import ua.com.andromeda.cinemaspringbootapp.model.Country;

import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, String> {
    Optional<Country> findByName(String name);
}
