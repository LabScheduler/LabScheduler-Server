package com.example.labschedulerserver.service;

import com.example.labschedulerserver.model.Role;

import java.util.Optional;

public interface RoleService {
    public Role findRoleByName(String name);
}
