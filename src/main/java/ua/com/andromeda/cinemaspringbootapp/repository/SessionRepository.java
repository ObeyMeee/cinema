package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.andromeda.cinemaspringbootapp.model.Session;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    @Query(value = "select t1.id, name, start_time, movie_details_id " +
            "from " +
            "   (select distinct on (name) sessions.*, production_year " +
            "   from sessions " +
            "   join movie_details md on sessions.movie_details_id = md.id) t1 " +
            "order by production_year desc", nativeQuery = true)
    List<Session> findUniqueSessionsByName();

    List<Session> findAllByMovieDetailsIdOrderByStartTime(@Param("id") String id);

    Page<Session> findAll(Pageable pageable);
}
