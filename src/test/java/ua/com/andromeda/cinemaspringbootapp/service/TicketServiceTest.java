package ua.com.andromeda.cinemaspringbootapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.andromeda.cinemaspringbootapp.dto.TicketDTO;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;
import ua.com.andromeda.cinemaspringbootapp.repository.TicketRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(args = {
        "--DATABASE_URL=jdbc:postgresql://localhost:5432/cinema",
        "--DATABASE_USERNAME=postgres",
        "--DATABASE_PASSWORD=1337",
        "--EMAIL=andrrromeda88@gmail.com",
        "--EMAIL_PASSWORD=zpvfssjoeklutmud",
        "--APPLICATION_URL=http://localhost:8080"
})
class TicketServiceTest {
    final TicketService target;
    @MockBean
    TicketRepository ticketRepository;

    @Autowired
    TicketServiceTest(TicketService ticketService) {
        this.target = ticketService;
    }

    @Test
    void isPlaceTaken_true() {
        int row = 2;
        int seat = 2;
        boolean actual = TicketService.isPlaceTaken(getTicketsDTOList(), row, seat);
        assertTrue(actual);
    }

    @Test
    void isPlaceTaken_false() {
        int row = 2;
        int seat = 1;
        boolean actual = TicketService.isPlaceTaken(getTicketsDTOList(), row, seat);
        assertFalse(actual);
    }

    @Test
    void isPlaceTaken_emptyList() {
        boolean actual = TicketService.isPlaceTaken(Collections.EMPTY_LIST, 1, 2);
        assertFalse(actual);
    }

    @Test
    void isPlaceTaken_negativeParams() {
        assertThrows(IllegalArgumentException.class,
                () -> TicketService.isPlaceTaken(getTicketsDTOList(), -1, 1),
                "Row or seat can't be a negative number");

        assertThrows(IllegalArgumentException.class,
                () -> TicketService.isPlaceTaken(getTicketsDTOList(), 1, -1),
                "Row or seat can't be a negative number");
    }


    @Test
    void saveAll() {
        List<Ticket> tickets = getTicketsList();
        target.saveAll(tickets);
        verify(ticketRepository, times(1)).saveAll(anyList());
    }

    private List<Ticket> getTicketsList() {
        return List.of(new Ticket(), new Ticket(), new Ticket());
    }

    private List<TicketDTO> getTicketsDTOList() {
        List<TicketDTO> tickets = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            TicketDTO ticket = new TicketDTO();
            ticket.setRow(i);
            ticket.setSeat(i);
            tickets.add(ticket);
        }
        return tickets;
    }

    @Test
    void findAllBySessionId_found() {
        when(ticketRepository.findAllBySessionId(anyString())).thenReturn(getTicketsList());
        int expected = 3;
        int actual = target.findAllBySessionId("123").size();
        assertEquals(expected, actual);
    }

    @Test
    void findAllBySessionId_notFound() {
        when(ticketRepository.findAllBySessionId(anyString())).thenReturn(Collections.EMPTY_LIST);
        int expected = 0;
        int actual = target.findAllBySessionId("123").size();
        assertEquals(expected, actual);
    }

    @Test
    void findAllByUserId_notFound() {
        when(ticketRepository.findAllTicketsGroupedBySessionAndUser(anyString())).thenReturn(Collections.EMPTY_LIST);
        int expected = 0;
        int actual = target.findAllByUserId("123").size();
        assertEquals(expected, actual);
    }
}