package ua.com.andromeda.cinemaspringbootapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.dto.TicketDTO;
import ua.com.andromeda.cinemaspringbootapp.model.Genre;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.service.GenreService;
import ua.com.andromeda.cinemaspringbootapp.service.SessionService;
import ua.com.andromeda.cinemaspringbootapp.service.TicketService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);
    private final SessionService sessionService;
    private final GenreService genreService;
    private final TicketService ticketService;

    @Autowired
    public MovieController(SessionService sessionService, GenreService genreService, TicketService ticketService) {
        this.sessionService = sessionService;
        this.genreService = genreService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public ModelAndView showAll(ModelAndView modelAndView,
                                Authentication authentication,
                                @PageableDefault(size = 6, sort = {"startTime"},
                                        direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Session> page = sessionService.findAll(authentication, pageable);
        modelAndView.addObject("page", page);
        modelAndView.setViewName("movies/schedule");
        return modelAndView;
    }

    @PreAuthorize("@sessionService.hasAccess(authentication, #id)")
    @GetMapping("/hall/{id}")
    public ModelAndView showSession(@PathVariable String id, ModelAndView modelAndView) {
        Session session = sessionService.findById(id);
        List<TicketDTO> tickets = ticketService.findAllBySessionId(id);

        modelAndView.addObject("tickets", tickets);
        modelAndView.addObject("movie", session);
        modelAndView.setViewName("movies/hall");
        return modelAndView;
    }

    @GetMapping("/unique")
    public ModelAndView showDistinctMovies(ModelAndView modelAndView) {
        List<Session> sessions = sessionService.findUniqueSessions();
        modelAndView.addObject("movies", sessions);
        modelAndView.setViewName("movies/distinct");
        return modelAndView;
    }

    @GetMapping("/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable String id, ModelAndView modelAndView) {
        Session session = sessionService.findById(id);
        modelAndView.addObject("movie", session);
        modelAndView.setViewName("movies/update-session");
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView showSessionForm(ModelAndView modelAndView) {
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        modelAndView.addObject("movie", new Session());
        modelAndView.setViewName("movies/add-session-form");
        return modelAndView;
    }

    @PostMapping("/new")
    public String save(Principal principal, Session session,
                       BindingResult bindingResult, @RequestParam("actors") String fullNames) {

        if (bindingResult.hasErrors()) {
            return "movies/add-session-form";
        }
        sessionService.save(session, fullNames);
        LOGGER.info("{} added session {} at {}", principal.getName(), session.getName(), session.getStartTime());
        return "redirect:/home";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id, Principal principal) {
        Session sessionToBeDeleted = sessionService.findById(id);
        String sessionName = sessionToBeDeleted.getName();
        LocalDateTime startTime = sessionToBeDeleted.getStartTime();
        sessionService.delete(sessionToBeDeleted);
        LOGGER.info("{} deleted session {} at {}", principal.getName(), sessionName, startTime);
        return "redirect:/movies";
    }

    @DeleteMapping("/movieDetailsId/{movieDetailsId}")
    public String deleteAll(@PathVariable String movieDetailsId, Principal principal) {
        Session session = sessionService.findDistinctByMovieDetailsId(movieDetailsId);
        sessionService.deleteAllByMovieDetailsId(movieDetailsId);
        LOGGER.info("{} deleted all sessions {} ", principal.getName(), session.getName());
        return "redirect:/movies/unique";
    }

    @PatchMapping("/update")
    public String update(Session session, Principal principal) {
        sessionService.save(session);
        LOGGER.info("{} changed session {} at {}", principal.getName(), session.getName(), session.getStartTime());
        return "redirect:/movies";
    }

    @PatchMapping("/enabled-one")
    public String enabled(@RequestParam String id, @RequestParam boolean enabled) {
        Session session = sessionService.findById(id);
        session.setEnabled(enabled);
        sessionService.save(session);
        return "redirect:/movies";
    }

    @PatchMapping
    public String enabledAll(@RequestParam String movieDetailsId, @RequestParam boolean enabled) {
        List<Session> sessions = sessionService.findAllByMovieDetailsId(movieDetailsId);
        sessionService.saveAll(sessions, enabled);
        return "redirect:/movies/unique";
    }
}
