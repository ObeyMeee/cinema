package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/hall/{id}")
    public String showSession(@PathVariable String id, Model model) {
        Session session = sessionService.findById(id);
        MovieDetails movieDetails = session.getMovieDetails();
        model.addAttribute("movie", session);
        model.addAttribute("movieDetails", movieDetails);
        return "movies/hall";
    }

}
