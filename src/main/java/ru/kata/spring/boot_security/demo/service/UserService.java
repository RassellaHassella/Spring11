package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    public User add(User user);

    public void createRolesIfNotExist();

    public List<User> usersList();

    public User update(User user);

    public void delete(Long id);

    public User getUser(Long id);

    public User findByEmail(String email);


}