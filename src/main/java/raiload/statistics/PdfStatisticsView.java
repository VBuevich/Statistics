package raiload.statistics;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import org.jboss.logging.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Class used to generate PDF for view
 *
 * @author vbuevich
 */
public class PdfStatisticsView {

    private static final Logger LOGGER = Logger.getLogger(PdfStatisticsView.class);

    /**
     * Method for dynamic generation of streamed content - PDF
     *
     * @param statistics list of statistics entities
     * @return
     */
    public static StreamedContent buildPdfDocument(ArrayList<Statistics> statistics) {

        Document document = new Document();
        StreamedContent sc = null;

        document.open();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            Paragraph head = new Paragraph("RailRoad site: Statistics for ticketing");
            head.setAlignment("Center");
            document.add(head);

            for (Statistics s : statistics) {
                Table table = new Table(2);

                table.addCell("When bought");
                table.addCell(s.getDatetime().toString());

                table.addCell("Name");
                table.addCell(s.getPassengerName());

                table.addCell("Surname");
                table.addCell(s.getPassengerSurname());

                table.addCell("Date of birth");
                table.addCell(s.getPassengerDob().toString());

                table.addCell("Email");
                table.addCell(s.getPassengerEmail());

                table.addCell("Train number");
                table.addCell(Integer.toString(s.getTrainNumber()));

                table.addCell("Type");
                table.addCell(s.getTrainType());

                table.addCell("From");
                table.addCell(s.getDepartureStation());

                table.addCell("To");
                table.addCell(s.getArrivalStation());

                table.addCell("Seat");
                table.addCell(s.getSeat());

                table.addCell("OneWay");
                table.addCell(s.getIsOneWay().toString());

                table.addCell("Departure date");
                table.addCell(s.getDepartureDate().toString());

                table.addCell("Departure time");
                table.addCell(s.getDepartureTime().toString());

                document.add(table);
                document.add(Chunk.NEWLINE);
            }

            document.close();
            sc = new DefaultStreamedContent(new ByteArrayInputStream(out.toByteArray()), "application/pdf");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return sc;
    }
}