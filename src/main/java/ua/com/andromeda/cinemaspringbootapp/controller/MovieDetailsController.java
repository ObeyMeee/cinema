package ua.com.andromeda.cinemaspringbootapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.mapper.ActorMapper;
import ua.com.andromeda.cinemaspringbootapp.model.Genre;
import ua.com.andromeda.cinemaspringbootapp.model.MovieDetails;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.service.GenreService;
import ua.com.andromeda.cinemaspringbootapp.service.MovieDetailsService;
import ua.com.andromeda.cinemaspringbootapp.service.SessionService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/movie-details")
public class MovieDetailsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieDetailsController.class);
    private final SessionService sessionService;
    private final MovieDetailsService movieDetailsService;
    private final GenreService genreService;
    private final ActorMapper actorMapper;

    @Autowired
    public MovieDetailsController(SessionService sessionService, MovieDetailsService movieDetailsService, GenreService genreService, ActorMapper actorMapper) {
        this.sessionService = sessionService;
        this.movieDetailsService = movieDetailsService;
        this.genreService = genreService;
        this.actorMapper = actorMapper;
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
        String actorsFullNames = actorMapper.mapActorsCollectionToString(movieDetails.getActors());
        List<Genre> genres = genreService.findAll();

        modelAndView.addObject("movieDetails", movieDetails);
        modelAndView.addObject("genres", genres);
        modelAndView.addObject("actors", actorsFullNames);
        modelAndView.setViewName("movie-details/update_form");
        return modelAndView;
    }

    @PutMapping("/update")
    public String update(MovieDetails movieDetails, BindingResult bindingResult,
                         Principal principal, @RequestParam("actors") String actorsFullNames) {

        if (bindingResult.hasErrors()) {
            return "movie-details/update_form";
        }
        movieDetailsService.save(movieDetails, actorsFullNames);
        LOGGER.info("{} changed {}", principal.getName(), movieDetails);
        return "redirect:/movies/unique";
    }
}
