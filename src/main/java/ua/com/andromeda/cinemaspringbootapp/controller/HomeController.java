package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String home(Model model) {
        List<Session> sessions = sessionService.getUniqueSessions();
        model.addAttribute("movies", sessions);
        return "home";
    }
}
