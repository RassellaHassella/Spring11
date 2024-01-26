package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
     List<UserDTO> index();
     Optional<UserDTO> show(Long id);

     void createRolesIfNotExist();

     Optional<UserDTO>  save(User user);
     Optional<UserDTO>  update(User updatedUser);
     void delete(Long id);
}
