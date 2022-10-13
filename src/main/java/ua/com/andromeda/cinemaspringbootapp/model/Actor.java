package ua.com.andromeda.cinemaspringbootapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "actors")
@Getter
@Setter
@ToString
public class Actor {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "full_name", unique = true, nullable = false)
    private String fullName;

    @ManyToMany(mappedBy = "actors")
    @ToString.Exclude
    private Set<MovieDetails> movieDetails;

    public Actor() {

    }

    public Actor(String fullName) {
        this.fullName = fullName;
    }
}
