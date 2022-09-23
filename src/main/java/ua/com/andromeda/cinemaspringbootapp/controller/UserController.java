package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }

    @PostMapping("/new")
    public String save(@Validated User user, BindingResult bindingResult) {
        System.out.println("user = " + user);
        if (bindingResult.hasErrors()) {
            return "users/form";
        }
        userService.save(user);
        return "redirect:/home";
    }
}
