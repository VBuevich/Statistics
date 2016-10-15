package raiload.statistics;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.jboss.logging.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

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

        Document document = new Document(); // new PDF document
        StreamedContent sc = null; // that is what we can show by Primefaces Extentions on JSF page

        if (statistics == null || statistics.isEmpty()) { // in case if Statistics is empty

            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                PdfWriter.getInstance(document, out);
                document.open();

                Paragraph head = new Paragraph("RailRoad site: Statistics for ticketing");
                head.setAlignment("Center");
                document.add(head);
                document.add(Chunk.NEWLINE);
                Paragraph head1 = new Paragraph("Statistics is empty for selected period of time. No tickets been sold.");
                head.setAlignment("Center");
                document.add(head1);

                document.close();
                sc = new DefaultStreamedContent(new ByteArrayInputStream(out.toByteArray()), "application/pdf");
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            return sc;
        }


        // GENERATING STATISTICS DATA
        ArrayList<String> emailList = new ArrayList<String>();
        ArrayList<String> trainList = new ArrayList<String>();
        ArrayList<String> departureList = new ArrayList<String>();
        ArrayList<String> arrivalList = new ArrayList<String>();

        for (Statistics s : statistics) {
            emailList.add(s.getPassengerEmail());
            trainList.add(Integer.toString(s.getTrainNumber()));
            departureList.add(s.getDepartureStation());
            arrivalList.add(s.getArrivalStation());
        }

        Map.Entry<String, Integer> bestCustomerMap = findMostFrequent(emailList); // most frequent traveler
        Map.Entry<String, Integer> bestTrain = findMostFrequent(trainList); // most occupied train
        Map.Entry<String, Integer> bestDeparture = findMostFrequent(departureList); // most wanted departure station
        Map.Entry<String, Integer> bestArrival = findMostFrequent(arrivalList); // most wanted arrival station

        String bestCustomerEmail = bestCustomerMap.getKey();
        String bestCustomer = "";
        for (Statistics s : statistics) {
            if (s.getPassengerEmail().equals(bestCustomerEmail)) {
                bestCustomer = s.getPassengerName() + " " + s.getPassengerSurname();
            }
        }
        // GENERATING STATISTICS PDF
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            Paragraph head = new Paragraph("RailRoad site: Statistics for ticketing");
            head.setAlignment("Center");
            document.add(head);
            document.add(Chunk.NEWLINE);

            PdfPTable statisticsTable = new PdfPTable(3);

            // Header
            statisticsTable.addCell("");
            statisticsTable.addCell("Top result");
            statisticsTable.addCell("Number of times");
            // Best customer
            statisticsTable.addCell("Best Customer");
            statisticsTable.addCell(bestCustomer);
            statisticsTable.addCell(bestCustomerMap.getValue().toString());
            // Best train
            statisticsTable.addCell("Most occupied train");
            statisticsTable.addCell(bestTrain.getKey());
            statisticsTable.addCell(bestTrain.getValue().toString());
            // Best departure
            statisticsTable.addCell("Most wanted Departure");
            statisticsTable.addCell(bestDeparture.getKey());
            statisticsTable.addCell(bestDeparture.getValue().toString());
            // Best arrival
            statisticsTable.addCell("Most wanted Arrival");
            statisticsTable.addCell(bestArrival.getKey());
            statisticsTable.addCell(bestArrival.getValue().toString());

            document.add(statisticsTable);
            document.add(Chunk.NEWLINE);

            Paragraph head1 = new Paragraph("Detailed Statistics : all tickets sold within the selected period");
            head1.setAlignment("Center");
            document.add(head1);
            document.add(Chunk.NEWLINE);

            for (Statistics s : statistics) {
                PdfPTable table = new PdfPTable(2);

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

    /**
     * Method to get most frequent string in collection and the number of its occurrences
     *
     * @param list collection of statistics`s items
     * @return pair of most frequent string and the number of its occurrences
     */
    private static Map.Entry<String, Integer> findMostFrequent(ArrayList<String> list) {

        Map<String, Integer> rating = new TreeMap<String, Integer>();
        for (String s : list) {

            if (rating.containsKey(s)) { // if we have already counted this string - lets increment his counter
                rating.put(s, (rating.get(s) +1));
            }
            else {
                rating.put(s, 1); // new entry, this string has just 1 ticket at the moment
            }
        }

        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : rating.entrySet()) // looking for most frequent string
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        return maxEntry; // pair of most frequent string and the number of times we met it
    }

}