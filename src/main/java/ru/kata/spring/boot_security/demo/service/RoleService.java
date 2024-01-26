package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
     Role save(Role role);

     List<Role> findAll();

     Optional<Role> findById(Long id);

     void delete(Long id);

     Optional<Role> findByRole(String role);

}
