package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.service.SessionService;

import java.util.List;

@Controller
public class HomeController {
    private final SessionService sessionService;

    @Autowired
    public HomeController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView) {
        List<Session> sessions = sessionService.findUniqueSessions();
        modelAndView.addObject("movies", sessions);
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
