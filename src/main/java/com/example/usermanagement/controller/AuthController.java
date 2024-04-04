package com.example.usermanagement.controller;

import com.example.usermanagement.dto.LoginDto;
import com.example.usermanagement.dto.LoginResponseDto;
import com.example.usermanagement.dto.changePassDto;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.service.AuthService;
import com.example.usermanagement.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @ResponseBody
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElseThrow();
            String token = JwtUtil.createToken(user);
            LoginResponseDto dto = new LoginResponseDto(email,token);

            return ResponseEntity.ok(dto);

        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(HttpServletRequest request,
                                         @RequestParam("email") String userEmail) {

        authService.resetPassword(userEmail);
        return ResponseEntity.ok("Email Sent");
    }

    @GetMapping("/changePassword")
    public ResponseEntity changePassword(@RequestBody changePassDto dto) throws Exception {
        authService.changePassword(dto.getToken(), dto.getPassword());
        return ResponseEntity.ok("Password Changed");
    }

}
