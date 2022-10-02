package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.RoleService;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;

import java.security.Principal;
import java.util.List;

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
    public String showList(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/new")
    public String showForm(@ModelAttribute User user, Principal principal, Model model) {
        if (principal != null) {
            String login = principal.getName();
            UserDetails userDetails = userService.loadUserByUsername(login);
            model.addAttribute("authorities", userDetails.getAuthorities());
        }
        return "users/form";
    }

    @PostMapping("/new")
    public String save(@Validated User user,
                       BindingResult bindingResult,
                       @RequestParam("roles") List<String> values) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(System.out::println);
            return "users/form";
        }
        values.add("ROLE_CUSTOMER");
        List<Role> roles = roleService.mapStringListToRoles(values);
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/home";
    }

    @GetMapping("/{id}")
    public String showProfile(@PathVariable String id, Model model) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("User is not founded"));
        return "users/profile";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
