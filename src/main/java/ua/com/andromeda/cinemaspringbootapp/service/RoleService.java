package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.repository.RoleRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<Role> mapStringListToRoles(List<String> roles) {
        return roles.stream()
                .map(roleRepository::findByName)
                .collect(Collectors.toSet());
    }

    public List<Role> findAllExceptOwner() {
        return roleRepository.findAllByNameIsNotLike("%OWNER%");
    }
}
