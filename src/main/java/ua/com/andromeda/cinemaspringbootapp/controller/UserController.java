package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.RoleService;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ModelAndView showList(ModelAndView modelAndView) {
        List<User> users = userService.findAll();
        modelAndView.addObject("users", users);
        modelAndView.setViewName("users/list");
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView showForm(@ModelAttribute User user, ModelAndView modelAndView) {
        List<Role> roles = roleService.findAllExceptOwner();
        modelAndView.addObject("roles", roles);
        modelAndView.setViewName("users/form");
        return modelAndView;
    }

    @PostMapping("/new")
    public String save(@Valid User user,
                       BindingResult bindingResult,
                       @RequestParam("role") List<String> values) {
        values.forEach(System.out::println);
        if (bindingResult.hasErrors()) {
            return "users/form";
        }
        Set<Role> roles = roleService.mapStringListToRoles(values);
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/home";
    }

    @GetMapping("/{id}")
    public ModelAndView showProfile(@PathVariable String id, ModelAndView modelAndView) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("User is not founded"));
        modelAndView.setViewName("users/profile");
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
