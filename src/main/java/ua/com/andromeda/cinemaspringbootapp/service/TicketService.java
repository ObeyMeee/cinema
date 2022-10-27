package ua.com.andromeda.cinemaspringbootapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.andromeda.cinemaspringbootapp.dto.Purchase;
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

    public static boolean isPlaceTaken(List<?> tickets, int row, int seat) {
        return tickets.stream()
                .map(TicketDTO.class::cast)
                .anyMatch(t -> t.getRow() == row && t.getSeat() == seat);
    }

    @Transactional
    public void saveAll(Collection<Ticket> tickets) {
        ticketRepository.saveAll(tickets);
    }

    public List<TicketDTO> findAllBySessionId(String id) {
        List<Ticket> tickets = ticketRepository.findAllBySessionId(id);
        return tickets.stream()
                .map(TicketDTO::new)
                .toList();
    }

    public List<Purchase> findAllByUserId(String id) {
        List<Tuple> ticketsTuple = ticketRepository.findAllTicketsGroupedBySessionAndUser(id);
        return ticketsTuple.stream()
                .map(tupleMapper::mapTupleToTicketDTO)
                .toList();
    }
}
