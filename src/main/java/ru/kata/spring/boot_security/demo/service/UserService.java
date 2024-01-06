package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    public List<UserDTO> index();
    public Optional<UserDTO> show(Long id);
    public Optional<UserDTO>  save(User user);
    public Optional<UserDTO>  update(User updatedUser);
    public void delete(Long id);
}
