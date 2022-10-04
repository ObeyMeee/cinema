package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.SessionService;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
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
            String login = principal.getName();
            User user = userService.findByLogin(login)
                    .orElseThrow(() -> new UsernameNotFoundException("Cannot find user with login ==> " + login));
            request.getSession().setAttribute("id", user.getId());
        }
        List<Session> sessions = sessionService.getUniqueSessions();
        modelAndView.addObject("movies", sessions);
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
