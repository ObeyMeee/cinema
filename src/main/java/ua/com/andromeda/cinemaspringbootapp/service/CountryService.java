package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.Country;
import ua.com.andromeda.cinemaspringbootapp.repository.CountryRepository;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Iterable<Country> findAll() {
        return countryRepository.findAll();
    }
}
