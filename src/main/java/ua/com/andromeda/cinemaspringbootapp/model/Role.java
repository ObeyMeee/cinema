package ua.com.andromeda.cinemaspringbootapp.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
public class Role {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "role")
    @ToString.Exclude
    private List<User> users;

    public void add(@NonNull User user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }
}
