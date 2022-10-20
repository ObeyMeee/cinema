package ua.com.andromeda.cinemaspringbootapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.dto.TicketDTO;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.RoleService;
import ua.com.andromeda.cinemaspringbootapp.service.TicketService;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;
import ua.com.andromeda.cinemaspringbootapp.validator.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final RoleService roleService;
    private final TicketService ticketService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService,
                          RoleService roleService,
                          TicketService ticketService,
                          UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.ticketService = ticketService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public ModelAndView showList(@RequestParam(value = "value", required = false) String searchValue,
                                 ModelAndView modelAndView,
                                 Principal principal,
                                 @PageableDefault Pageable pageable) {
        Page<User> users;
        String login = principal.getName();
        if (searchValue == null) {
            users = userService.findAllWhereUsersHaveSameOrLessRoles(login, pageable);
        } else {
            users = userService.findAllWhereUsersHaveSameOrLessRolesAndEmailLikeOrLoginLike(login, searchValue, pageable);
        }
        modelAndView.addObject("page", users);
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
    public String save(@Valid User user,
                       BindingResult bindingResult,
                       Principal principal,
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
        if (principal == null) {
            LOGGER.info("{} has been registered", user);
        }else {
            LOGGER.info("{} registered {}", principal.getName(), user.getLogin());
        }
        return "redirect:/home";
    }

    @GetMapping("/{id}")
    public ModelAndView showProfile(@PathVariable String id, ModelAndView modelAndView) {
        User user = userService.findById(id);
        List<TicketDTO> tickets = ticketService.findAllByUserId(id);
        modelAndView.addObject("user", user);
        modelAndView.addObject("tickets", tickets);
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
        userService.save(user);
        LOGGER.info("{} updated user: {}", principal.getName(), user.getLogin());
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id, Principal principal) {
        User user = userService.findById(id);
        String login = user.getLogin();
        userService.delete(id);
        LOGGER.info("{} deleted user: {}", principal.getName(), login);
        return "redirect:/users";
    }
}
