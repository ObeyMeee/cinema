package ua.com.andromeda.cinemaspringbootapp.utils.mail;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;
import ua.com.andromeda.cinemaspringbootapp.model.User;
import ua.com.andromeda.cinemaspringbootapp.utils.PdfService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.List;

@Component
public class EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final PdfService pdfService;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender, PdfService pdfService) {
        this.javaMailSender = javaMailSender;
        this.pdfService = pdfService;
    }

    @SneakyThrows
    public void sendEmailTicket(List<Ticket> tickets, EmailAction action) {
        String fileName;
        String subject;
        if (action == EmailAction.VERIFY_EMAIL) {
            fileName = "emails/verify_email.html";
            subject = "Confirm your email address";
        } else {
            fileName = "emails/tickets_purchase.html";
            subject = "Tickets purchase";
        }
        pdfService.createFile(tickets);
        String content = getContent(fileName);
        Ticket ticket = tickets.get(0);
        Session session = ticket.getSession();
        User user = ticket.getUser();
        content = content.replace("Sir", user.getLogin())
                .replace("dateValue", session.getStartTime().toString())
                .replace("filmValue", session.getName());
        MimeMessage mimeMessage = getMimeMessageTicket(user, subject, content);
        javaMailSender.send(mimeMessage);
    }

    @SneakyThrows
    public void sendEmail(User user, EmailAction action) {
        String fileName;
        String subject;
        if (action == EmailAction.VERIFY_EMAIL) {
            fileName = "emails/verify_email.html";
            subject = "Confirm your email address";
        } else {
            fileName = "emails/tickets_purchase.html";
            subject = "Tickets purchase";
        }
        String content = getContent(fileName);
        content = content.replace("Sir", user.getLogin());
        MimeMessage mimeMessage = getMimeMessage(user, subject, content);
        javaMailSender.send(mimeMessage);
    }

    private MimeMessage getMimeMessageTicket(User user, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        helper.setText(content, true);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setFrom("andromedacinema@kh.ua");
        File file = new File("tickets.pdf");
        helper.addAttachment("tickets.pdf", file);
        return mimeMessage;
    }

    private MimeMessage getMimeMessage(User user, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        mimeMessage.setContent(content, "text/html");
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setFrom("andromedacinema@kh.ua");
        return mimeMessage;
    }

    private String getContent(String fileName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream resource = classLoader.getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource));
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            sb.append(line).append("\n");
            line = reader.readLine();
        }
        return sb.toString();
    }
}
