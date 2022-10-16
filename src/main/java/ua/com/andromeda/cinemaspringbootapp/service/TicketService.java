package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;
import ua.com.andromeda.cinemaspringbootapp.repository.TicketRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public static boolean isPlaceFree(List<?> tickets, int row, int seat) {
        List<Ticket> tickets1 = tickets.stream().map(Ticket.class::cast).toList();
        for (Ticket ticket : tickets1) {
            if (ticket.getRow() == row && ticket.getSeat() == seat) {
                return false;
            }
        }
        System.out.println("tickets1 = " + tickets1);
        System.out.println("row = " + row);
        System.out.println("seat = " + seat);
        return true;
    }

    @Transactional
    public void saveAll(Collection<Ticket> tickets) {
        ticketRepository.saveAll(tickets);
    }

    public List<Ticket> findAllBySessionId(String id) {
        return ticketRepository.findAllBySessionId(id);
    }
}
