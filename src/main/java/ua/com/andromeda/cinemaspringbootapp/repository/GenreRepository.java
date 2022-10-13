package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.com.andromeda.cinemaspringbootapp.model.Genre;

@Repository
public interface GenreRepository extends CrudRepository<Genre, String> {
}
