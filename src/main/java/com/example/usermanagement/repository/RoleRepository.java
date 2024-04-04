package com.example.usermanagement.repository;

import com.example.usermanagement.entity.Role;
import com.example.usermanagement.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);
}
