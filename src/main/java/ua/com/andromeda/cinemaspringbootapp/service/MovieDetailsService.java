package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.mapper.ActorMapper;
import ua.com.andromeda.cinemaspringbootapp.model.Actor;
import ua.com.andromeda.cinemaspringbootapp.model.Country;
import ua.com.andromeda.cinemaspringbootapp.model.Media;
import ua.com.andromeda.cinemaspringbootapp.model.MovieDetails;
import ua.com.andromeda.cinemaspringbootapp.repository.CountryRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.MediaRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.MovieDetailsRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieDetailsService {
    private final MovieDetailsRepository movieDetailsRepository;
    private final ActorMapper actorMapper;
    private final CountryRepository countryRepository;
    private final MediaRepository mediaRepository;

    @Autowired
    public MovieDetailsService(MovieDetailsRepository movieDetailsRepository, ActorMapper actorMapper, CountryRepository countryRepository, MediaRepository mediaRepository) {
        this.movieDetailsRepository = movieDetailsRepository;
        this.actorMapper = actorMapper;
        this.countryRepository = countryRepository;
        this.mediaRepository = mediaRepository;
    }

    public MovieDetails getMovieDetailsById(String id) {
        return movieDetailsRepository.findById(id).get();
    }

    @Transactional
    public void save(MovieDetails movieDetails, String actorsFullNames) {
        Set<Actor> actors = actorMapper.mapFullNamesToActors(actorsFullNames);
        movieDetails.setActors(actors);
        Country country = movieDetails.getCountry();
        Optional<Country> optionalCountry = countryRepository.findByName(country.getName());
        optionalCountry.ifPresent(movieDetails::setCountry);
        Media media = movieDetails.getMedia();
        Optional<Media> optionalMedia = mediaRepository.findByTrailerAndPoster(media.getTrailer(), media.getPoster());
        optionalMedia.ifPresent(movieDetails::setMedia);
        movieDetailsRepository.save(movieDetails);
    }
}
