package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.mapper.ActorMapper;
import ua.com.andromeda.cinemaspringbootapp.model.*;
import ua.com.andromeda.cinemaspringbootapp.repository.CountryRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.MediaRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.SessionRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public Page<Session> findAll(Authentication authentication, Pageable pageable) {
        if (authentication == null || authentication.getAuthorities().size() == 1) {
            return findAllEnabled( true, pageable);
        }
        return findAll(pageable);
    }

    private Page<Session> findAll(Pageable pageable) {
        return sessionRepository.findAll(pageable);
    }

    private Page<Session> findAllEnabled(boolean enabled, Pageable pageable) {
        return sessionRepository.findAllByEnabled(enabled, pageable);
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
    public void deleteById(Session session) {
        List<Ticket> tickets = session.getTickets();
        tickets.forEach(ticket -> ticket.setSession(null));
        sessionRepository.delete(session);
    }

    public Session findDistinctByMovieDetailsId(String movieDetailsId) {
        return sessionRepository.findDistinctByMovieDetailsId(movieDetailsId);
    }

    @Transactional
    public void deleteAllByMovieDetailsId(String movieDetailsId) {
        List<Session> sessions = sessionRepository.findAllByMovieDetailsId(movieDetailsId);
        sessions.stream().flatMap(session -> session.getTickets().stream()).forEach(ticket -> ticket.setSession(null));
        sessionRepository.deleteAllByMovieDetailsId(movieDetailsId);
    }

    public void saveAll(List<Session> sessions, boolean enabled) {
        sessions.forEach(session -> session.setEnabled(enabled));
        sessionRepository.saveAll(sessions);
    }

    public boolean hasAccess(Authentication authentication, String sessionId) {
        Session session = findById(sessionId);
        return session.isEnabled() || authentication == null || authentication.getAuthorities().size() > 1;
    }

}
