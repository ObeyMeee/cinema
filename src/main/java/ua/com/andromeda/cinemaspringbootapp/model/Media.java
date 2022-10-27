package ua.com.andromeda.cinemaspringbootapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Media {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(columnDefinition = "TEXT")
    private String poster;

    @Column(columnDefinition = "TEXT")
    private String trailer;

    @ToString.Exclude
    @OneToOne(mappedBy = "media", cascade = CascadeType.ALL)
    private MovieDetails movieDetails;
}
