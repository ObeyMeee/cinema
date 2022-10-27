package ua.com.andromeda.cinemaspringbootapp.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.model.Role;
import ua.com.andromeda.cinemaspringbootapp.repository.RoleRepository;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<Role> mapStringListToRoles(Collection<String> roles) {
        return roles.stream()
                .map(roleRepository::findByName)
                .collect(Collectors.toSet());
    }
}
