package ua.com.andromeda.cinemaspringbootapp.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class MovieDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String description;

    @Min(1)
    private Integer duration;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id")
    private Media media;
    private String director;

    @Min(1850)
    @Max(2023)
    private Integer productionYear;

    @OneToMany(mappedBy = "movieDetails", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Session> sessions;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "movie_details_actors",
            joinColumns = @JoinColumn(name = "movie_details_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @ToString.Exclude
    private Set<Actor> actors;
    @ManyToMany
    @JoinTable(
            name = "movie_details_genres",
            joinColumns = @JoinColumn(name = "movie_details_id"),
            inverseJoinColumns = @JoinColumn(name = "genres_id")
    )
    @ToString.Exclude
    private Set<Genre> genres;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
    @ToString.Exclude
    private Country country;

    public void add(Actor actor) {
        if (actors == null) {
            actors = new LinkedHashSet<>();
        }
        actors.add(actor);
    }

    public void add(Genre genre) {
        if (genres == null) {
            genres = new LinkedHashSet<>();
        }
        genres.add(genre);
    }

    public void add(Session session) {
        if (sessions == null) {
            sessions = new ArrayList<>();
        }
        sessions.add(session);
    }
}