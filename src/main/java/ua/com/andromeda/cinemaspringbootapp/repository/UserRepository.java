package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.andromeda.cinemaspringbootapp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
