package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.andromeda.cinemaspringbootapp.model.Media;

public interface MediaRepository extends JpaRepository<Media, String> {
}
