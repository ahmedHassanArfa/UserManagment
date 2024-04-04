package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserDto;

public interface AuthService {

    Long registerUser(UserDto userDto);

    void resetPassword(String email);

    void changePassword(String token, String password) throws Exception;

}
