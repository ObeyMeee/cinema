package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.andromeda.cinemaspringbootapp.model.Actor;

@Repository
public interface ActorRepository extends CrudRepository<Actor, String> {
    Actor findByFullName(String fullName);
}
