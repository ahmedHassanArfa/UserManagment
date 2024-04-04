package com.example.usermanagement.service;

import com.example.usermanagement.dto.UserDto;

import java.util.List;

public interface UserService {

    Long create(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    void delete(Long id);

    UserDto findById(Long id);

    List<UserDto> retrieveUsers(String firstName, String lastName, List<Long> userIds, String role);

}
