package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/movies")
public class MovieController {
    private final SessionService sessionService;
    private final MovieDetailsService movieDetailsService;

    @Autowired
    public MovieController(SessionService sessionService, MovieDetailsService movieDetailsService) {
        this.sessionService = sessionService;
        this.movieDetailsService = movieDetailsService;
    }

    @GetMapping("/{id}")
    public String getMovieDetails(@PathVariable String id, Model model) {
        List<Session> sessions = sessionService.findAllByMovieDetailsId(id);
        MovieDetails movieDetails = movieDetailsService.getMovieDetailsById(id);
        model.addAttribute("movies", sessions);
        model.addAttribute("movieDetails", movieDetails);
        return "movies/movie-details";
    }
}
