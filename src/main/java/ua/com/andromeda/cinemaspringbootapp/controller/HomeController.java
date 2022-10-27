package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.model.VerificationToken;
import ua.com.andromeda.cinemaspringbootapp.service.SessionService;
import ua.com.andromeda.cinemaspringbootapp.service.TokenService;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;
import ua.com.andromeda.cinemaspringbootapp.validator.VerificationTokenValidator;

import java.util.List;

@Controller
public class HomeController {
    private final SessionService sessionService;
    private final TokenService tokenService;
    private final UserService userService;
    private final VerificationTokenValidator tokenValidator;

    @Autowired
    public HomeController(SessionService sessionService,
                          TokenService tokenService,
                          UserService userService,
                          VerificationTokenValidator tokenValidator) {

        this.sessionService = sessionService;
        this.tokenService = tokenService;
        this.userService = userService;
        this.tokenValidator = tokenValidator;
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView) {
        List<Session> sessions = sessionService.findUniqueSessions();
        modelAndView.addObject("movies", sessions);
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/registration-сonfirm")
    public ModelAndView confirmRegistration(@RequestParam("token") String token, ModelAndView modelAndView) {

        VerificationToken verificationToken = tokenService.getVerificationToken(token);
        if (!tokenValidator.validate(verificationToken)) {
            modelAndView.setViewName("redirect:/home");
            return modelAndView;
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userService.update(user);

        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }
}
