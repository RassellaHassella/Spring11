package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    void add(User user);

    void createRolesIfNotExist();

     List<User> usersList();


    void delete(Long id);

    User getUser(Long id);

    User findByEmail(String email);


    void update(User user, List<Role> roles);
}