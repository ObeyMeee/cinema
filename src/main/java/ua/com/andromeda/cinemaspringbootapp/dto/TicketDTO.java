package ua.com.andromeda.cinemaspringbootapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;

@Getter
@Setter
@NoArgsConstructor
public class TicketDTO {
    private int row;
    private int seat;

    public TicketDTO(Ticket ticket) {
        this.row = ticket.getRow();
        this.seat = ticket.getSeat();
    }
}
