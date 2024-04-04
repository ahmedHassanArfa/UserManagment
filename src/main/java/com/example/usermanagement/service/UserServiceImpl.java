package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.entity.Role;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.RoleRepository;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.util.PasswordGeneratorUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public Long create(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setBirthdate(userDto.getBirthdate());
        user.setGender(userDto.getGender());

        // Set user role
        Role userRole = roleRepository.findByName("user");
        user.setRoles(Collections.singletonList(userRole));

        // Generate random password
        String password = PasswordGeneratorUtil.generateRandomPassword(8);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        // Send email notification
        emailService.sendEmail(user.getEmail(), "Account Created",
                "Your account has been created. Please use the following credentials to log in:\n" +
                        "Email: " + user.getEmail() + "\n" +
                        "Password: " + password);
        return user.getId();
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setBirthdate(userDto.getBirthdate());
        user.setGender(userDto.getGender());
        userRepository.save(user);
        return userDto;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setBirthdate(user.getBirthdate());
        userDto.setGender(user.getGender());
        return userDto;
    }

    @Override
    public List<UserDto> retrieveUsers(String firstName, String lastName, List<Long> userIds, String role) {
        return null;
    }
}
