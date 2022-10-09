package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.SessionService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HomeController {
    private final SessionService sessionService;

    @Autowired
    public HomeController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView, @AuthenticationPrincipal User user, HttpServletRequest request) {
        if (user != null) {
            request.getSession().setAttribute("id", user.getId());
        }
        List<Session> sessions = sessionService.getUniqueSessions();
        modelAndView.addObject("movies", sessions);
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
