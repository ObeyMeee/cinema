package ua.com.andromeda.cinemaspringbootapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "countries")
@Getter
@Setter
@ToString
public class Country {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<MovieDetails> movieDetails;
}
