package ru.kata.spring.boot_security.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDTO> index() {
        return convertToUserDTO(userRepository.findAll());
    }

    private List<UserDTO> convertToUserDTO(List<User> users) {
        List<UserDTO> usersDTO = users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        for (int i = 0; i < usersDTO.size(); i++) {
            usersDTO.get(i).getRoles().clear();
            for (Role role : users.get(i).getRoles()) {
                usersDTO.get(i).addRoles(role.getRole());
            }
        }
        return usersDTO;
    }

    @Override
    public Optional<UserDTO> show(Long id) {

        if (userRepository.findById(id).isEmpty()) {
            return Optional.of(null);
        } else {
        return Optional.ofNullable(convertToUserDTO(List.of(userRepository.getById(id))).get(0));
        }
    }
    @Override
    public void createRolesIfNotExist() {
        if (roleRepository.findByRole("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role(1L, "ROLE_USER"));
        }
        if (roleRepository.findByRole("ADMIN").isEmpty()) {
            roleRepository.save(new Role(2L, "ROLE_ADMIN"));
        }
    }
    @Transactional
    @Override
    public Optional<UserDTO> save(User user) {

        if ( userRepository.findByEmail(user.getEmail()) != null) {
            return Optional.ofNullable(null);
        }

        if (user.getPassword() == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        return Optional.ofNullable(convertToUserDTO(List.of(user)).get(0));
    }

    @Transactional
    @Override
    public Optional<UserDTO> update(User user) {
        if (user.getPassword().isEmpty()) {
            user.setPassword(show(user.getId()).get().getPassword());
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        return Optional.ofNullable(convertToUserDTO(List.of(user)).get(0));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("Пользователя с таким email не существует: " + email);
        }
        return user.get();
    }
}
