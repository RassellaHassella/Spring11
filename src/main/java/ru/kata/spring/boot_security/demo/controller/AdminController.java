package ru.kata.spring.boot_security.demo.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;


import java.util.List;

@Controller
public class AdminController {
    @GetMapping("/admin")
    public String findAll(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "admin";
    }

    @GetMapping("/user")
    public String showUser(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(value = {"/", "/login"})
    public String toLogin() {
        return "login";
    }


}
