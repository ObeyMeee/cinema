package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.MovieDetails;
import ua.com.andromeda.cinemaspringbootapp.repository.MovieDetailsRepository;

@Service
public class MovieDetailsService {
    private final MovieDetailsRepository movieDetailsRepository;

    @Autowired
    public MovieDetailsService(MovieDetailsRepository movieDetailsRepository) {
        this.movieDetailsRepository = movieDetailsRepository;
    }

    public MovieDetails getMovieDetailsById(String id) {
        return movieDetailsRepository.findById(id).get();
    }
}
