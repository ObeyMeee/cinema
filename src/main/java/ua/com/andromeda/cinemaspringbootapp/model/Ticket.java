package ua.com.andromeda.cinemaspringbootapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@ToString
public class Ticket {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private Integer price;
    private Integer row;
    private Integer seat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_type_id")
    private TicketType type;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

}
