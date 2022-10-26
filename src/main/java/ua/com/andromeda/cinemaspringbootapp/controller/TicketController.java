package ua.com.andromeda.cinemaspringbootapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;
import ua.com.andromeda.cinemaspringbootapp.model.TicketType;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.service.SessionService;
import ua.com.andromeda.cinemaspringbootapp.service.TicketService;
import ua.com.andromeda.cinemaspringbootapp.service.UserService;
import ua.com.andromeda.cinemaspringbootapp.utils.EmailSenderService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class.getName());
    private final TicketService ticketService;
    private final UserService userService;
    private final SessionService sessionService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public TicketController(TicketService ticketService,
                            UserService userService,
                            SessionService sessionService, EmailSenderService emailSenderService) {

        this.ticketService = ticketService;
        this.userService = userService;
        this.sessionService = sessionService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/new")
    public String purchaseTickets(HttpServletRequest request,
                                  @RequestParam("sessionId") String sessionId,
                                  Principal principal) {

        String login = principal.getName();
        User user = userService.findByLogin(login);
        Session session = sessionService.findById(sessionId);
        String[] rows = request.getParameterValues("row");
        String[] seats = request.getParameterValues("seat");
        String[] prices = request.getParameterValues("price");
        String[] ticketTypes = request.getParameterValues("ticketType");
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < rows.length; i++) {
            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setRow(Integer.parseInt(rows[i]));
            ticket.setSeat(Integer.parseInt(seats[i]));
            ticket.setPrice(Integer.parseInt(prices[i]));
            ticket.setType(TicketType.valueOf(ticketTypes[i]));
            ticket.setSession(session);
            tickets.add(ticket);
        }
        ticketService.saveAll(tickets);
        emailSenderService.sendTickets(tickets);
        LOGGER.info("{} bought {}", login, tickets);
        return "redirect:/home";
    }
}
