package ru.artemka.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.artemka.demo.exception.DataNotFoundException;
import ru.artemka.demo.model.Role;
import ru.artemka.demo.model.RoleEntity;
import ru.artemka.demo.model.User;
import ru.artemka.demo.repository.RoleRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRolesService {

    private final RoleRepository roleRepository;

    public void addRoles(User user, Set<Role> rolesToAdd) {
        Set<RoleEntity> userRoles = user.getRoles();
        rolesToAdd.forEach(r -> userRoles.add(getRoleEntity(r)));
    }

    public RoleEntity getRoleEntity(Role role) {
        return roleRepository.findByRoleName(role)
                .orElseThrow(() -> new DataNotFoundException("Wrong role"));
    }
}
