package ua.com.andromeda.cinemaspringbootapp.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
public class MovieDetails {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String description;
    private Integer duration;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id")
    private Media media;
    private String director;
    private Integer productionYear;

    @OneToMany(mappedBy = "movieDetails", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Session> sessions;
    @ManyToMany
    @JoinTable
            (name = "movie_details_actors",
            joinColumns = @JoinColumn(name = "movie_details_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @ToString.Exclude
    private Set<Actor> actors;
    @ManyToMany
    @JoinTable(
            name = "movieDetails_genres",
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