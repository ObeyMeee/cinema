package ua.com.andromeda.cinemaspringbootapp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.mapper.ActorMapper;
import ua.com.andromeda.cinemaspringbootapp.model.*;
import ua.com.andromeda.cinemaspringbootapp.repository.CountryRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.MediaRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.MovieDetailsRepository;

import java.util.Optional;
import java.util.Set;

@Component
public class DatabaseUtils {
    private final ActorMapper actorMapper;
    private final CountryRepository countryRepository;
    private final MediaRepository mediaRepository;
    private final MovieDetailsRepository movieDetailsRepository;

    @Autowired
    public DatabaseUtils(ActorMapper actorMapper, CountryRepository countryRepository, MediaRepository mediaRepository, MovieDetailsRepository movieDetailsRepository) {
        this.actorMapper = actorMapper;
        this.countryRepository = countryRepository;
        this.mediaRepository = mediaRepository;
        this.movieDetailsRepository = movieDetailsRepository;
    }

    public void setSession(Session session, String actorsFullNames) {
        MovieDetails movieDetails = session.getMovieDetails();
        setCountryIfPresentFromDb(movieDetails);
        Optional<MovieDetails> optionalMovieDetailsFromDb =
                movieDetailsRepository.findByDirectorAndProductionYearAndCountry(
                        movieDetails.getDirector(), movieDetails.getProductionYear(), movieDetails.getCountry());
        if (optionalMovieDetailsFromDb.isPresent()) {
            session.setMovieDetails(optionalMovieDetailsFromDb.get());
            return;
        }
        Set<Actor> actors = actorMapper.mapFullNamesToActors(actorsFullNames);
        movieDetails.setActors(actors);
        setMediaIfPresentFromDb(movieDetails);
    }

    private void setCountryIfPresentFromDb(MovieDetails movieDetails) {
        Country country = movieDetails.getCountry();
        Optional<Country> optionalCountry = countryRepository.findByName(country.getName());
        if (optionalCountry.isPresent()) {
            movieDetails.setCountry(optionalCountry.get());
        } else {
            Country saved = countryRepository.save(country);
            movieDetails.setCountry(saved);
        }
    }

    private void setMediaIfPresentFromDb(MovieDetails movieDetails) {
        Media media = movieDetails.getMedia();
        Optional<Media> optionalMedia = mediaRepository.findByTrailerAndPoster(media.getTrailer(), media.getPoster());
        optionalMedia.ifPresent(movieDetails::setMedia);
    }

    public void setMovieDetails(MovieDetails movieDetails, String actorsFullNames) {
        Set<Actor> actors = actorMapper.mapFullNamesToActors(actorsFullNames);
        movieDetails.setActors(actors);
        setCountryIfPresentFromDb(movieDetails);
        setMediaIfPresentFromDb(movieDetails);
    }
}
