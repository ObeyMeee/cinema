package ua.com.andromeda.cinemaspringbootapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.dto.Purchase;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.RoleService;
import ua.com.andromeda.cinemaspringbootapp.service.TicketService;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;
import ua.com.andromeda.cinemaspringbootapp.validator.UserValidator;
import ua.com.andromeda.cinemaspringbootapp.verification.OnRegistrationCompleteEvent;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final RoleService roleService;
    private final TicketService ticketService;
    private final UserValidator userValidator;
    private final ApplicationEventPublisher eventPublisher;
    @Value("${APPLICATION_URL}")
    private String appUrl;

    @Autowired
    public UserController(UserService userService, RoleService roleService,
                          TicketService ticketService, UserValidator userValidator,
                          ApplicationEventPublisher eventPublisher) {

        this.userService = userService;
        this.roleService = roleService;
        this.ticketService = ticketService;
        this.userValidator = userValidator;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public ModelAndView showList(ModelAndView modelAndView, Principal principal) {
        List<User> users = userService.findAllWhereUsersHaveSameOrLessRolesCount(principal.getName());
        modelAndView.addObject("users", users);
        modelAndView.setViewName("users/list");
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

    @GetMapping("/new")
    public ModelAndView showForm(@ModelAttribute User user, ModelAndView modelAndView) {
        List<Role> roles = roleService.findAllExceptOwner();
        modelAndView.addObject("roles", roles);
        modelAndView.setViewName("users/register_form");
        return modelAndView;
    }

    @GetMapping("/update/{id}")
    public ModelAndView showFormUpdate(@PathVariable String id, ModelAndView modelAndView) {
        User user = userService.findById(id);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("users/update_form");
        return modelAndView;
    }

    @PostMapping("/new")
    public String save(@Valid User user, BindingResult bindingResult,
                       @RequestParam("role") List<String> roles) {

        String errorMessage = userValidator.validateRegisteringUser(user);
        if (!errorMessage.isEmpty()) {
            bindingResult.addError(new ObjectError("globalError", errorMessage));
        }

        if (bindingResult.hasErrors()) {
            return "users/register_form";
        }
        userService.save(user, roles);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));
        LOGGER.info("{} has been registered", user);
        return "redirect:/home";
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

        userService.save(user);
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