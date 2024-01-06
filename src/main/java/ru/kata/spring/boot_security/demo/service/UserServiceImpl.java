package ru.kata.spring.boot_security.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private UserDAO userDAO;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userDAO = userDAO;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDTO> index() {

        return convertToUserDTO(userDAO.index());
    }

    private List<UserDTO> convertToUserDTO(List<User> users) {
        List<UserDTO> usersDTO = users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        for (int i = 0; i < usersDTO.size(); i++) {
            usersDTO.get(i).getRoles().clear();
            for (Role role : users.get(i).getRoles()) {
                usersDTO.get(i).addRoles(role.getValue());
            }
        }
        return usersDTO;
    }

    @Override
    public Optional<UserDTO> show(Long id) {
        List<User> users = userDAO.show(id);
        if (users == null) {
            return Optional.of(null);
        }
        return Optional.ofNullable(convertToUserDTO(users).get(0));
    }

    @Transactional
    @Override
    public Optional<UserDTO> save(User user) {
        User users = userDAO.findByEmail(user.getEmail());
        if (users != null) {
            return Optional.ofNullable(null);
        }

        if (user.getPassword() == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getRawPassword()));
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userDAO.save(user);
        return Optional.ofNullable(convertToUserDTO(List.of(user)).get(0));
    }

    @Transactional
    @Override
    public Optional<UserDTO> update(User updatedUser) {
        if (updatedUser.getRawPassword().isEmpty()) {
            updatedUser.setPassword(show(updatedUser.getId()).get().getPassword());
        } else {
            updatedUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getRawPassword()));
        }
        userDAO.update(updatedUser);
        return Optional.ofNullable(convertToUserDTO(List.of(updatedUser)).get(0));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userDAO.delete(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDAO.findByEmail(email);

        if (user == null)
            throw new UsernameNotFoundException("User not found!");

        return user;
    }
}
