package ua.com.andromeda.cinemaspringbootapp.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import ua.com.andromeda.cinemaspringbootapp.model.Session;
import ua.com.andromeda.cinemaspringbootapp.model.Ticket;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfService {

    public void createFile(List<Ticket> tickets) throws IOException, DocumentException, URISyntaxException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("tickets.pdf"));
        document.open();
        Path path = Paths.get(getClass().getClassLoader().getResource("static/logo.png").toURI());
        Image image = Image.getInstance(path.toAbsolutePath().toString());
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);
        Font courierBlack25 = FontFactory.getFont(FontFactory.COURIER, 25, BaseColor.BLACK);
        Font courierBoldBlack25 = FontFactory.getFont(FontFactory.COURIER_BOLD, 25, BaseColor.BLACK);
        StringBuilder sb = new StringBuilder();
        for (Ticket ticket : tickets) {
            sb.append(ticket.getRow()).append(" row ")
                    .append(ticket.getSeat()).append(" seat ")
                    .append(ticket.getType()).append('\n');
        }
        Session session = tickets.get(0).getSession();
        String date = session.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        Chunk sessionInfoChunk = new Chunk(session.getName() + " " + date, courierBoldBlack25);
        Chunk placesChunk = new Chunk(sb.toString(), courierBlack25);
        Paragraph headline = new Paragraph(sessionInfoChunk);
        Paragraph places = new Paragraph(placesChunk);
        places.setAlignment(Element.ALIGN_CENTER);
        document.add(headline);
        document.add(Chunk.NEWLINE);
        document.add(places);
        document.close();
    }
}
