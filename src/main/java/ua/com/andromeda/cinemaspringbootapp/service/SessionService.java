package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.mapper.ActorMapper;
import ua.com.andromeda.cinemaspringbootapp.model.*;
import ua.com.andromeda.cinemaspringbootapp.repository.ActorRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.CountryRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.MediaRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.SessionRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final CountryRepository countryRepository;

    private final ActorMapper actorMapper;
    private final MediaRepository mediaRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, ActorMapper actorMapper,
                          CountryRepository countryRepository, MediaRepository mediaRepository) {

        this.sessionRepository = sessionRepository;
        this.countryRepository = countryRepository;
        this.mediaRepository = mediaRepository;
        this.actorMapper = actorMapper;
    }

    public List<Session> findUniqueSessions() {
        return sessionRepository.findUniqueSessionsByName();
    }

    public List<Session> findAllByMovieDetailsId(String id) {
        return sessionRepository.findAllByMovieDetailsIdOrderByStartTime(id);
    }

    public Session findById(String id) {
        return sessionRepository.findById(id).orElse(null);
    }

    public Page<Session> findAll(Pageable pageable) {
        return sessionRepository.findAll(pageable);
    }

    @Transactional
    public void save(Session session, String actorsFullNames) {
        Set<Actor> actors = actorMapper.mapFullNamesToActors(actorsFullNames);
        session.getMovieDetails().setActors(actors);
        MovieDetails movieDetails = session.getMovieDetails();
        Country country = movieDetails.getCountry();
        Optional<Country> optionalCountry = countryRepository.findByName(country.getName());
        optionalCountry.ifPresent(movieDetails::setCountry);
        Media media = movieDetails.getMedia();
        Optional<Media> optionalMedia = mediaRepository.findByTrailerAndPoster(media.getTrailer(), media.getPoster());
        optionalMedia.ifPresent(movieDetails::setMedia);
        sessionRepository.save(session);
    }

    @Transactional
    public void save(Session session) {
        sessionRepository.save(session);
    }
    @Transactional
    public void deleteById(String id) {
        sessionRepository.deleteById(id);
    }

    @Transactional
    public void deleteSessionsByName(String name) {
        sessionRepository.deleteAllByName(name);
    }
}
