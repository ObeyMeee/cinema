package ua.com.andromeda.cinemaspringbootapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.dto.Purchase;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.model.VerificationToken;
import ua.com.andromeda.cinemaspringbootapp.service.RoleService;
import ua.com.andromeda.cinemaspringbootapp.service.TicketService;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;
import ua.com.andromeda.cinemaspringbootapp.utils.verification.OnRegistrationCompleteEvent;
import ua.com.andromeda.cinemaspringbootapp.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final RoleService roleService;
    private final TicketService ticketService;
    private final UserValidator userValidator;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSource messages;

    @Autowired
    public UserController(UserService userService, RoleService roleService,
                          TicketService ticketService, UserValidator userValidator,
                          ApplicationEventPublisher eventPublisher, MessageSource messages) {
        this.userService = userService;
        this.roleService = roleService;
        this.ticketService = ticketService;
        this.userValidator = userValidator;
        this.eventPublisher = eventPublisher;
        this.messages = messages;
    }

    @GetMapping
    public ModelAndView showList(ModelAndView modelAndView, Principal principal) {
        List<User> users = userService.findAllWhereUsersHaveSameOrLessRoles(principal.getName());
        modelAndView.addObject("users", users);
        modelAndView.setViewName("users/list");
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView showForm(@ModelAttribute User user, ModelAndView modelAndView) {
        List<Role> roles = roleService.findAllExceptOwner();
        modelAndView.addObject("roles", roles);
        modelAndView.setViewName("users/register_form");
        return modelAndView;
    }

    @PostMapping("/new")
    public String save(@Valid User user, BindingResult bindingResult,
                       Principal principal, HttpServletRequest request,
                       @RequestParam("role") List<String> values) {

        String errorMessage = userValidator.validateRegisteringUser(user);
        if (!errorMessage.isEmpty()) {
            bindingResult.addError(new ObjectError("globalError", errorMessage));
        }

        if (bindingResult.hasErrors()) {
            return "users/register_form";
        }
        Set<Role> roles = roleService.mapStringListToRoles(values);
        user.setRoles(roles);
        userService.save(user);
        String appUrl = request.getContextPath();
        System.out.println("user = " + user);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
        if (principal == null) {
            LOGGER.info("{} has been registered", user);
        } else {
            LOGGER.info("{} registered {}", principal.getName(), user.getLogin());
        }
        return "redirect:/home";
    }

    @GetMapping("/registrationConfirm")
    public ModelAndView confirmRegistration(WebRequest request,
                                            ModelAndView modelAndView,
                                            @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            modelAndView.addObject("message", message);
            modelAndView.setViewName("badUser");
            return modelAndView;
        }

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            String message = messages.getMessage("auth.message.expired", null, locale);
            modelAndView.addObject("message", message);
            modelAndView.setViewName("badUser");
            return modelAndView;
        }

        user.setEnabled(true);
        userService.update(user);
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }

    @GetMapping("/{userLogin}")
    @PreAuthorize("@userService.hasAccess(#userLogin, authentication)")
    public ModelAndView showProfile(@PathVariable String userLogin, ModelAndView modelAndView) {
        User user = userService.findByLogin(userLogin);
        List<Purchase> purchases = ticketService.findAllByUserId(user.getId());
        modelAndView.addObject("user", user);
        modelAndView.addObject("purchases", purchases);
        modelAndView.setViewName("users/profile");
        return modelAndView;
    }

    @GetMapping("/update/{id}")
    public ModelAndView showFormUpdate(@PathVariable String id, ModelAndView modelAndView) {
        User user = userService.findById(id);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("users/update_form");
        return modelAndView;
    }

    @PutMapping("/update")
    public String update(@Valid User user, BindingResult bindingResult, Principal principal) {
        String errorMessage = userValidator.validateUpdatedUser(user);
        if (!errorMessage.isEmpty()) {
            bindingResult.addError(new ObjectError("globalError", errorMessage));
        }
        if (bindingResult.hasErrors()) {
            return "users/update_form";
        }
        userService.update(user);
        LOGGER.info("{} updated user: {}", principal.getName(), user.getLogin());
        return "redirect:/users";
    }

    @PatchMapping("/enabled")
    public String block(@RequestParam String id, @RequestParam boolean enabled) {
        User user = userService.findById(id);
        user.setEnabled(enabled);
        userService.update(user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id, Principal principal) {
        User user = userService.findById(id);
        userService.delete(user);
        LOGGER.info("{} deleted user: {}", principal.getName(), user.getLogin());
        return "redirect:/users";
    }
}
