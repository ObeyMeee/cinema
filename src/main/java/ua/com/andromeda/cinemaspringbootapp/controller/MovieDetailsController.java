package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String showMovieDetails(@PathVariable String id, Model model) {
        List<Session> sessions = sessionService.findAllByMovieDetailsId(id);
        MovieDetails details = movieDetailsService.getMovieDetailsById(id);
        model.addAttribute("movieDetails", details);
        model.addAttribute("movies", sessions);
        return "movie-details/description";
    }
}
