package ua.com.andromeda.cinemaspringbootapp.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfService {

    public void createFile(List<Ticket> tickets) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("tickets.pdf"));
        document.open();
        addLogo(document);
        Session session = tickets.get(0).getSession();
        addHeadline(document, session);
        document.add(Chunk.NEWLINE);
        addTickets(tickets, document);
        document.close();
    }

    private void addLogo(Document document) throws IOException, DocumentException {
        File file = getCopyLogoFromResources();
        Image image = Image.getInstance(file.getAbsolutePath());
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);
    }

    private File getCopyLogoFromResources() throws IOException {
        ClassPathResource resource = new ClassPathResource("/static/logo.png");
        InputStream inputStream = resource.getInputStream();
        File file = new File("logo_from_input_stream.png");
        FileUtils.copyInputStreamToFile(inputStream, file);
        return file;
    }

    private void addHeadline(Document document, Session session) throws DocumentException {
        Font courierBoldBlack25 = FontFactory.getFont(FontFactory.COURIER_BOLD, 25, BaseColor.BLACK);
        String date = session.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        Chunk sessionInfoChunk = new Chunk(session.getName() + " " + date, courierBoldBlack25);
        Paragraph headline = new Paragraph(sessionInfoChunk);
        document.add(headline);
    }


    private void addTickets(List<Ticket> tickets, Document document) throws DocumentException {
        String ticketsInfo = getTicketsInfo(tickets);
        Font courierBlack25 = FontFactory.getFont(FontFactory.COURIER, 25, BaseColor.BLACK);
        Chunk placesChunk = new Chunk(ticketsInfo, courierBlack25);
        Paragraph places = new Paragraph(placesChunk);
        places.setAlignment(Element.ALIGN_CENTER);
        document.add(places);
    }

    private String getTicketsInfo(List<Ticket> tickets) {
        StringBuilder sb = new StringBuilder();
        for (Ticket ticket : tickets) {
            sb.append(ticket.getRow()).append(" row ")
                    .append(ticket.getSeat()).append(" seat ")
                    .append(ticket.getType()).append('\n');
        }
        return sb.toString();
    }

}

