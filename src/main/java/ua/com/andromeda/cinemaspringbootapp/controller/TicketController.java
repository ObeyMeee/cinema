package ua.com.andromeda.cinemaspringbootapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.TicketService;

import java.util.Collection;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/new")
    public String purchaseTicket(Collection<Ticket> tickets, @AuthenticationPrincipal User user) {
        tickets.forEach(ticket -> ticket.setUser(user));
        ticketService.saveAll(tickets);
        return "redirect:/home";
    }
}
