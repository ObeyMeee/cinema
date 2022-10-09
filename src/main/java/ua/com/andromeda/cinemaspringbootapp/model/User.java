package ua.com.andromeda.cinemaspringbootapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull(message = "login cannot be empty")
    @Size(min = 4, message = "Login should contain at least 4 characters")
    @Column(unique = true)
    private String login;

    @Email(message = "Incorrect email address")
    @Column(unique = true)
    private String email;

    @Size(min = 5, message = "Password should contain at least 5 characters")
    @NotNull(message = "password cannot be empty")
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private Set<Role> roles;
    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH, CascadeType.DETACH})
    @ToString.Exclude
    private Set<Ticket> tickets;

    public void add(Ticket ticket) {
        if (tickets == null) {
            tickets = new HashSet<>();
        }
        tickets.add(ticket);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PrePersist
    public void logSave() {
        LOGGER.info("Saved user : {}", this);
    }

}
