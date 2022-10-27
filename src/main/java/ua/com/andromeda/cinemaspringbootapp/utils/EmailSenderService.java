package ua.com.andromeda.cinemaspringbootapp.utils;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;
import ua.com.andromeda.cinemaspringbootapp.model.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.format.DateTimeFormatter;
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
    public void sendTickets(List<Ticket> tickets) {
        Ticket ticket = tickets.get(0);
        Session session = ticket.getSession();
        User user = ticket.getUser();
        String content = getMessageBody("emails/tickets_purchase.html");
        String date = session.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        content = replaceValues(content, session.getName(), user.getLogin(), date);
        pdfService.createFile(tickets);
        MimeMessage message = getMimeMessageWithAttachment(user, content);
        javaMailSender.send(message);
    }

    private String getMessageBody(String fileName) throws IOException {
        BufferedReader reader = getReaderFromResource(fileName);
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            sb.append(line).append("\n");
            line = reader.readLine();
        }
        return sb.toString();
    }

    private String replaceValues(String content, String sessionName, String login, String date) {
        return content.replace("Sir", login)
                .replace("dateValue", date)
                .replace("filmValue", sessionName);
    }

    private BufferedReader getReaderFromResource(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream resource = classLoader.getResourceAsStream(fileName);
        assert resource != null;
        return new BufferedReader(new InputStreamReader(resource));
    }

    private MimeMessage getMimeMessageWithAttachment(User user, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        helper.setText(content, true);
        setMessageHeader(helper, user.getEmail(), "Tickets purchase");
        helper.addAttachment("tickets.pdf", new File("tickets.pdf"));
        return mimeMessage;
    }

    @SneakyThrows
    public void sendVerificationEmail(User user, String confirmationUrl) {
        String content = getMessageBody("emails/verify_email.html");
        content = content.replace("Sir", user.getLogin())
                .replace("confirmationUrlValue", confirmationUrl);
        MimeMessage message = getMimeMessage(user.getEmail(), content);
        javaMailSender.send(message);
    }


    private MimeMessage getMimeMessage(String toEmail, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setContent(content, "text/html");
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        setMessageHeader(helper, toEmail, "Confirm your email address");
        return mimeMessage;
    }

    private void setMessageHeader(MimeMessageHelper helper, String toEmail, String subject) throws MessagingException {
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setFrom("noreply@kh.ua");
    }
}
