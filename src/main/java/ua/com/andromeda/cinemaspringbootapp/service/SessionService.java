package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.repository.MediaRepository;
import ua.com.andromeda.cinemaspringbootapp.repository.SessionRepository;

import java.util.List;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, MediaRepository mediaRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getUniqueSessions() {
        return sessionRepository.findUniqueSessionsByName();
    }

    public List<Session> findAllByMovieDetailsId(String id) {
        return sessionRepository.findAllByMovieDetailsId(id);
    }
}
