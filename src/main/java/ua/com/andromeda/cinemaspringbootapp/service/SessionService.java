package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.repository.SessionRepository;

import java.util.List;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getUniqueSessions() {
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
}
