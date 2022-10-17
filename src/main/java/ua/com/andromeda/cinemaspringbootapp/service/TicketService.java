package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.dto.TicketDTO;
import ua.com.andromeda.cinemaspringbootapp.mapper.TupleMapper;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;
import ua.com.andromeda.cinemaspringbootapp.repository.TicketRepository;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TupleMapper tupleMapper;

    @Autowired
    public TicketService(TicketRepository ticketRepository, TupleMapper tupleMapper) {
        this.ticketRepository = ticketRepository;
        this.tupleMapper = tupleMapper;
    }

    public static boolean isPlaceFree(List<?> tickets, int row, int seat) {
        List<Ticket> tickets1 = tickets.stream().map(Ticket.class::cast).toList();
        for (Ticket ticket : tickets1) {
            if (ticket.getRow() == row && ticket.getSeat() == seat) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public void saveAll(Collection<Ticket> tickets) {
        ticketRepository.saveAll(tickets);
    }

    public List<Ticket> findAllBySessionId(String id) {
        return ticketRepository.findAllBySessionId(id);
    }

    public List<TicketDTO> findAllByUserId(String id) {

        List<Tuple> ticketsTuple = ticketRepository.findAllTicketsGroupedBySessionAndUser(id);
        return ticketsTuple.stream().map(tupleMapper::mapTupleToTicketDTO).toList();
    }
}
