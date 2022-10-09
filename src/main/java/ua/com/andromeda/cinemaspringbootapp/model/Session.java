package ua.com.andromeda.cinemaspringbootapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@ToString
public class Session {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;
    private LocalDateTime startTime;

    @OneToMany(mappedBy = "session")
    @ToString.Exclude
    private List<Ticket> tickets;

    @ManyToOne
    private MovieDetails movieDetails;
}
