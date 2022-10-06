package ua.com.andromeda.cinemaspringbootapp.model;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket_types")
@Getter
public enum TicketType {
    GOOD, LUX;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String name;

    @OneToMany(mappedBy = "type")
    private List<Ticket> tickets;

    public void add(Ticket ticket) {
        if (tickets == null) {
            tickets = new ArrayList<>();
        }
        tickets.add(ticket);
    }
}
