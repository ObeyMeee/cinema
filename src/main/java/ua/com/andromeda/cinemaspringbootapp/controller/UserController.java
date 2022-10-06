package ua.com.andromeda.cinemaspringbootapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.RoleService;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;

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

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ModelAndView showList(ModelAndView modelAndView, Principal principal, @PageableDefault Pageable pageable) {
        Page<User> users = userService.findAllWhereUsersHaveSameOrLessRoles(principal, pageable);
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
                       @RequestParam("role") List<String> values) {
        if (bindingResult.hasErrors()) {
            return "users/register_form";
        }
        Set<Role> roles = roleService.mapStringListToRoles(values);
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/home";
    }

    @GetMapping("/{id}")
    public ModelAndView showProfile(@PathVariable String id, ModelAndView modelAndView) {
        User user = userService.findById(id);
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
        if (bindingResult.hasErrors()) {
            return "users/update_form";
        }
        LOGGER.info("{} updated user: {}", principal.getName(), user);
        userService.update(user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
