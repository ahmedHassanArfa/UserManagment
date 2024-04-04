package com.example.usermanagement.dto;

import com.example.usermanagement.entity.Role;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private LocalDate birthdate;

    private String gender;
    private List<RoleDto> roles;

}
