package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.model.MovieDetails;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.service.SessionService;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private final SessionService sessionService;

    @Autowired
    public MovieController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public ModelAndView showAll(ModelAndView modelAndView,
                                @PageableDefault(size = 6, sort = {"startTime"}, direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Session> page = sessionService.findAll(pageable);
        modelAndView.addObject("page", page);
        modelAndView.setViewName("movies/schedule");
        return modelAndView;
    }

    @GetMapping("/hall/{id}")
    public ModelAndView showSession(@PathVariable String id, ModelAndView modelAndView) {
        Session session = sessionService.findById(id);
        MovieDetails movieDetails = session.getMovieDetails();
        modelAndView.addObject("movie", session);
        modelAndView.addObject("movieDetails", movieDetails);
        modelAndView.setViewName("movies/hall");
        return modelAndView;
    }

}
