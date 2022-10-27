package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.MovieDetails;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;
import ua.com.andromeda.cinemaspringbootapp.repository.SessionRepository;
import ua.com.andromeda.cinemaspringbootapp.utils.DatabaseUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;
    private final DatabaseUtils databaseUtils;

    @Autowired
    public SessionService(SessionRepository sessionRepository, DatabaseUtils databaseUtils) {
        this.sessionRepository = sessionRepository;
        this.databaseUtils = databaseUtils;
    }


    public boolean hasAccess(Authentication authentication, String sessionId) {
        Session session = findById(sessionId);
        return session.isEnabled() || authentication == null || authentication.getAuthorities().size() > 1;
    }

    public Session findById(String id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find session with id " + id));
    }

    public List<Session> findUniqueSessions() {
        return sessionRepository.findUniqueSessionsByName();
    }

    public List<Session> findAllByMovieDetailsId(String id) {
        return sessionRepository.findAllByMovieDetailsIdOrderByStartTime(id);
    }

    public Page<Session> findAll(Authentication authentication, Pageable pageable) {
        if (authentication == null || authentication.getAuthorities().size() == 1) {
            return findAllEnabled(pageable);
        }
        return findAll(pageable);
    }

    private Page<Session> findAll(Pageable pageable) {
        return sessionRepository.findAll(pageable);
    }

    private Page<Session> findAllEnabled(Pageable pageable) {
        return sessionRepository.findAllByEnabled(true, pageable);
    }


    public Session findDistinctByMovieDetailsId(String movieDetailsId) {
        return sessionRepository.findDistinctByMovieDetailsId(movieDetailsId);
    }


    @Transactional
    public void save(Session session, String actorsFullNames) {
        MovieDetails movieDetails = session.getMovieDetails();
        databaseUtils.setMovieDetails(movieDetails, actorsFullNames);
        sessionRepository.save(session);
    }

    @Transactional
    public void save(Session session) {
        sessionRepository.save(session);
    }

    public void saveAll(List<Session> sessions, boolean enabled) {
        sessions.forEach(session -> session.setEnabled(enabled));
        sessionRepository.saveAll(sessions);
    }

    @Transactional
    public void delete(Session session) {
        List<Ticket> tickets = session.getTickets();
        tickets.forEach(ticket -> ticket.setSession(null));
        sessionRepository.delete(session);
    }

    @Transactional
    public void deleteAllByMovieDetailsId(String movieDetailsId) {
        List<Session> sessions = sessionRepository.findAllByMovieDetailsId(movieDetailsId);
        sessions.stream()
                .flatMap(session -> session.getTickets().stream())
                .forEach(ticket -> ticket.setSession(null));
        sessionRepository.deleteAllByMovieDetailsId(movieDetailsId);
    }
}
