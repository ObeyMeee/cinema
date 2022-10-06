package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.andromeda.cinemaspringbootapp.model.Media;

@Repository
public interface MediaRepository extends JpaRepository<Media, String> {
}
