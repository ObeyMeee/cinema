package ua.com.andromeda.cinemaspringbootapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.model.Country;
import ua.com.andromeda.cinemaspringbootapp.model.Genre;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;
import ua.com.andromeda.cinemaspringbootapp.service.CountryService;
import ua.com.andromeda.cinemaspringbootapp.service.GenreService;
import ua.com.andromeda.cinemaspringbootapp.service.SessionService;
import ua.com.andromeda.cinemaspringbootapp.service.TicketService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);
    private final SessionService sessionService;
    private final CountryService countryService;
    private final GenreService genreService;
    private final TicketService ticketService;

    @Autowired
    public MovieController(SessionService sessionService,
                           CountryService countryService,
                           GenreService genreService,
                           TicketService ticketService) {

        this.sessionService = sessionService;
        this.countryService = countryService;
        this.genreService = genreService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public ModelAndView showAll(ModelAndView modelAndView,
                                @PageableDefault(size = 6,
                                        sort = {"startTime"},
                                        direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Session> page = sessionService.findAll(pageable);
        modelAndView.addObject("page", page);
        modelAndView.setViewName("movies/schedule");
        return modelAndView;
    }

    @GetMapping("/hall/{id}")
    public ModelAndView showSession(@PathVariable String id, ModelAndView modelAndView) {
        Session session = sessionService.findById(id);
        List<Ticket> tickets = ticketService.findAllBySessionId(id);
        modelAndView.addObject("tickets", tickets);
        modelAndView.addObject("movie", session);

        modelAndView.setViewName("movies/hall");
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView showSessionForm(ModelAndView modelAndView) {
        Iterable<Country> countries = countryService.findAll();
        Iterable<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        modelAndView.addObject("countries", countries);
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
    public String delete(@PathVariable String id) {
        sessionService.deleteById(id);
        return "redirect:/movies";
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

    @DeleteMapping("/name/{name}")
    public String deleteAll(@PathVariable String name) {
        sessionService.deleteSessionsByName(name);
        return "redirect:/movies/unique";
    }

    @PatchMapping("/update")
    public String update(Session session, Principal principal) {
        sessionService.save(session);
        LOGGER.info("{} changed session {} at {}", principal.getName(), session.getName(), session.getStartTime());
        return "redirect:/movies";
    }

}
