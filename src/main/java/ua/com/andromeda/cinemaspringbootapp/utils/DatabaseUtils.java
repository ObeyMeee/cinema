package ua.com.andromeda.cinemaspringbootapp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.mapper.ActorMapper;
import ua.com.andromeda.cinemaspringbootapp.model.Actor;
import ua.com.andromeda.cinemaspringbootapp.model.Country;
import ua.com.andromeda.cinemaspringbootapp.model.Media;
import ua.com.andromeda.cinemaspringbootapp.model.MovieDetails;
import ua.com.andromeda.cinemaspringbootapp.repository.CountryRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.MediaRepository;

import java.util.Optional;
import java.util.Set;

@Component
public class DatabaseUtils {
    private final ActorMapper actorMapper;
    private final CountryRepository countryRepository;
    private final MediaRepository mediaRepository;

    @Autowired
    public DatabaseUtils(ActorMapper actorMapper, CountryRepository countryRepository, MediaRepository mediaRepository) {
        this.actorMapper = actorMapper;
        this.countryRepository = countryRepository;
        this.mediaRepository = mediaRepository;
    }

    public void setMovieDetails(MovieDetails movieDetails, String actorsFullNames) {
        Set<Actor> actors = actorMapper.mapFullNamesToActors(actorsFullNames);
        movieDetails.setActors(actors);
        setCountryIfPresentFromDb(movieDetails);
        setMediaIfPresentFromDb(movieDetails);
    }

    private void setMediaIfPresentFromDb(MovieDetails movieDetails) {
        Media media = movieDetails.getMedia();
        Optional<Media> optionalMedia = mediaRepository.findByTrailerAndPoster(media.getTrailer(), media.getPoster());
        optionalMedia.ifPresent(movieDetails::setMedia);
    }

    private void setCountryIfPresentFromDb(MovieDetails movieDetails) {
        Country country = movieDetails.getCountry();
        Optional<Country> optionalCountry = countryRepository.findByName(country.getName());
        optionalCountry.ifPresent(movieDetails::setCountry);
    }
}
