package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.entity.PasswordResetToken;
import com.example.usermanagement.entity.Role;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.PasswordResetTokenRepository;
import com.example.usermanagement.repository.RoleRepository;
import com.example.usermanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, EmailService emailService, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Transactional
    public Long registerUser(UserDto userDto) {
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
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        emailService.sendEmail(user.getEmail(), "Reset Password",  "/api/auth/changePassword?token=" + token);
    }

    @Transactional
    @Override
    public void changePassword(String token, String password) throws Exception {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token).orElseThrow();
        if(isTokenExpired(passToken)) {
            throw new Exception("Token Expired");
        } else {
            User user = userRepository.findByEmail(passToken.getUser().getEmail()).orElseThrow();
            user.setPassword(passwordEncoder.encode(password));
        }
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    private void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(null, token, user, DateUtils.addDays(new Date(), 3));
        passwordResetTokenRepository.save(myToken);
    }

}
