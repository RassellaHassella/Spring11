package ru.kata.spring.boot_security.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;

import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@Controller
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping("/user")
    public String showUser(@RequestParam(name = "id", required = false) Long id, Model model, Principal principal) {
        User user;
        if (id != null) {
            user = userService.getUser(id);
        } else {
            String email = principal.getName();
            user = userService.findByEmail(email);
        }
        model.addAttribute("user", user);
        return "user";
    }
}
