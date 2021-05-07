package ru.artemka.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.artemka.demo.exception.DataNotFoundException;
import ru.artemka.demo.model.Role;
import ru.artemka.demo.model.RoleEntity;
import ru.artemka.demo.model.User;
import ru.artemka.demo.repository.RoleRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRolesService {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void createRoles() {
        if(roleRepository.findAll().isEmpty()) {
            List<RoleEntity> roleEntityList = List.of(
                    new RoleEntity(Role.UNCONFIRMED),
                    new RoleEntity(Role.USER),
                    new RoleEntity(Role.ADMIN));
            roleRepository.saveAll(roleEntityList);
        }
    }

    public void addRoles(User user, Set<Role> rolesToAdd) {
        Set<RoleEntity> userRoles = user.getRoles();
        rolesToAdd.forEach(r -> userRoles.add(getRoleEntity(r)));
    }

    public RoleEntity getRoleEntity(Role role) {
        return roleRepository.findByRoleName(role)
                .orElseThrow(() -> new DataNotFoundException("Wrong role"));
    }
}
