package com.example.usermanagement.dto;

import com.example.usermanagement.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class RoleDto {
    private Long id;

    private String name;

    private List<UserDto> users;

}
