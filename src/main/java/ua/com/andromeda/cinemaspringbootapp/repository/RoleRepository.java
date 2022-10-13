package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.andromeda.cinemaspringbootapp.model.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByName(String name);

    List<Role> findAllByNameIsNotLike(String name);
}
