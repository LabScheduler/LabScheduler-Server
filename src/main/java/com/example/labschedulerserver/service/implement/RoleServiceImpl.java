package com.example.labschedulerserver.service.implement;

import com.example.labschedulerserver.model.Role;
import com.example.labschedulerserver.repository.RoleRepository;
import com.example.labschedulerserver.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }
}
