package ru.kata.spring.boot_security.demo.controller;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;


import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final RoleRepository roleRepository;
    private final UserServiceImpl userService;

    public AdminController(RoleRepository roleRepository, UserServiceImpl userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping
    public String getUsersList(@AuthenticationPrincipal User user, Model model) {
        List<User> list = userService.usersList();
        model.addAttribute("usersList", list);
        model.addAttribute("newUser", new User());
        model.addAttribute("newRole", new Role());
        model.addAttribute("authUser", user);
        model.addAttribute("allUsers", list);
        return "admin";
    }


    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("editUser", user);
        userService.update(user, (List<Role>) user.getRoles());
        return "redirect:/admin";
    }


    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user,
                          @ModelAttribute("newRole") Role role) {
        userService.add(user);
        return "redirect:/admin";
    }

    @PostMapping("/save")
    public String save(User user, @ModelAttribute("newRole") Role role) {
        userService.update(user, (List<Role>) role);
        return "redirect:/admin";
    }

    @PostMapping("/remove")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("admin/get")
    public User get(long id) {
        return userService.getUser(id);
    }
}