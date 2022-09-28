package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.andromeda.cinemaspringbootapp.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
