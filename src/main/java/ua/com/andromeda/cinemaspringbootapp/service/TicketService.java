package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;
import ua.com.andromeda.cinemaspringbootapp.repository.TicketRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public static Ticket createTicket() {
        return new Ticket();
    }

    @Transactional
    public void saveAll(Collection<Ticket> tickets) {
        ticketRepository.saveAll(tickets);
    }
}
