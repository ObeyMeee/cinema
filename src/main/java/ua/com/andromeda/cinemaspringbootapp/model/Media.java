package ua.com.andromeda.cinemaspringbootapp.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Media {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String poster;
    private String trailer;

    @OneToOne(mappedBy = "media", cascade = CascadeType.ALL)
    private MovieDetails movieDetails;

    @Override
    public String toString() {
        return "Media{" +
                "id='" + id + '\'' +
                ", poster='" + poster + '\'' +
                ", trailer='" + trailer + '\'' +
                '}';
    }
}
