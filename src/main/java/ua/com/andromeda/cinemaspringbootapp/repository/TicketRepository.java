package ua.com.andromeda.cinemaspringbootapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;

import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findAllBySessionId(String id);

    @Query(value =
            "select sum(price) as total_price, count(*) as quantity, s.start_time as session_date, s.name as session_name " +
                    "from tickets " +
                    "join sessions s on tickets.session_id = s.id " +
                    "where user_id = :id " +
                    "group by s.start_time, s.name",
            nativeQuery = true)
    List<Tuple> findAllTicketsGroupedBySessionAndUser(@Param("id") String id);
}
