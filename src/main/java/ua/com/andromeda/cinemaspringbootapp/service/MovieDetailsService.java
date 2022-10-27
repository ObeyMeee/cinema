package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.MovieDetails;
import ua.com.andromeda.cinemaspringbootapp.repository.MovieDetailsRepository;
import ua.com.andromeda.cinemaspringbootapp.utils.DatabaseUtils;

import javax.transaction.Transactional;

@Service
public class MovieDetailsService {
    private final MovieDetailsRepository movieDetailsRepository;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public MovieDetailsService(MovieDetailsRepository movieDetailsRepository, DatabaseUtils databaseUtils) {
        this.movieDetailsRepository = movieDetailsRepository;
        this.databaseUtils = databaseUtils;
    }

    public MovieDetails getMovieDetailsById(String id) {
        return movieDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find movie details with id " + id));
    }

    @Transactional
    public void save(MovieDetails movieDetails, String actorsFullNames) {
        databaseUtils.setMovieDetails(movieDetails, actorsFullNames);
        movieDetailsRepository.save(movieDetails);
    }
}
