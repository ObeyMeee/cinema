package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.SessionService;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public HomeController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView, Principal principal, HttpServletRequest request) {
        if (principal != null) {
            User user = userService.findByLogin(principal.getName());
            HttpSession session = request.getSession();
            if (session.getAttribute("id") == null) {
                session.setAttribute("id", user.getId());
            }
        }

        List<Session> sessions = sessionService.findUniqueSessions();
        modelAndView.addObject("movies", sessions);
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
