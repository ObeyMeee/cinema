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
        ClassPathResource resource = new ClassPathResource("/static/logo.png");
        InputStream inputStream = resource.getInputStream();
        File file = new File("logo_from_input_stream.png");
        FileUtils.copyInputStreamToFile(inputStream, file);
        Image image = Image.getInstance(file.getAbsolutePath());
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
