package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.model.MovieDetails;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.service.MovieDetailsService;
import ua.com.andromeda.cinemaspringbootapp.service.SessionService;

import java.util.List;

@Controller
@RequestMapping("/movie-details")
public class MovieDetailsController {
    private final SessionService sessionService;
    private final MovieDetailsService movieDetailsService;

    public MovieDetailsController(SessionService sessionService, MovieDetailsService movieDetailsService) {
        this.sessionService = sessionService;
        this.movieDetailsService = movieDetailsService;
    }

    @GetMapping("/{id}")
    public ModelAndView showMovieDetails(@PathVariable String id, ModelAndView modelAndView) {
        List<Session> sessions = sessionService.findAllByMovieDetailsId(id);
        MovieDetails details = movieDetailsService.getMovieDetailsById(id);
        modelAndView.addObject("movieDetails", details);
        modelAndView.addObject("movies", sessions);
        modelAndView.setViewName("movie-details/description");
        return modelAndView;
    }

    @GetMapping("/update/{id}")
    public ModelAndView showUpdateForm(@PathVariable String id, ModelAndView modelAndView) {
        MovieDetails movieDetails = movieDetailsService.getMovieDetailsById(id);
        modelAndView.addObject("movieDetails", movieDetails);
        modelAndView.setViewName("movie-details/update_form");
        return modelAndView;
    }

}
