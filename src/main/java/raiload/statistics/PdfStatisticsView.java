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
        Map.Entry<String, Integer> bestCustomerMap = getBestCustomer(statistics); // most frequent traveler`s email
        String bestCustomerEmail = bestCustomerMap.getKey();
        String bestCustomer = "";
        for (Statistics s : statistics) { // looking for his name and surname
            if (s.getPassengerEmail().equals(bestCustomerEmail)) {
                bestCustomer = s.getPassengerName() + " " + s.getPassengerSurname(); // building name + surname string
            }
        }

        Map.Entry<Integer, Integer> bestTrain = getBestTrain(statistics); // most frequent traveler
        Map.Entry<String, Integer> bestDeparture = getBestDeparture(statistics); // most frequent traveler
        Map.Entry<String, Integer> bestArrival = getBestArrival(statistics); // most frequent traveler

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
            statisticsTable.addCell(bestTrain.getKey().toString());
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
     * Method to get most frequent traveler from statistics
     *
     * @param statistics received statistics
     * @return pair of passengers` email and the number of tickets he has bought
     */
    private static Map.Entry<String, Integer> getBestCustomer(ArrayList<Statistics> statistics) {

        Map<String, Integer> rating = new TreeMap<String, Integer>();
        for (Statistics s : statistics) {
            String email = s.getPassengerEmail();
            if (rating.containsKey(email)) { // if we have already counted this passenger - lets increment his counter
                rating.put(email, (rating.get(email) +1));
            }
            else {
                rating.put(email, 1); // new entry, this passenger has just 1 ticket at the moment
            }
        }

        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : rating.entrySet()) // looking for most frequent traveler
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        return maxEntry; // pair of most frequent travellers` email and the number of times he has bought tickets
    }

    /**
     * Method to get most occupied train from statistics
     *
     * @param statistics received statistics
     * @return pair of train number and the number of sold places on it
     */
    private static Map.Entry<Integer, Integer> getBestTrain(ArrayList<Statistics> statistics) {

        Map<Integer, Integer> rating = new TreeMap<Integer, Integer>();
        for (Statistics s : statistics) {
            Integer trainNumber = s.getTrainNumber();
            if (rating.containsKey(trainNumber)) { // if we have already counted this train - lets increment his counter
                rating.put(trainNumber, (rating.get(trainNumber) +1));
            }
            else { // new entry, this train has just one passenger
                rating.put(trainNumber, 1);
            }
        }

        Map.Entry<Integer, Integer> maxEntry = null;

        for (Map.Entry<Integer, Integer> entry : rating.entrySet()) // looking for most occupied train
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        return maxEntry; // pair of train number and the number of sold places on it
    }

    /**
     * Method to get most wanted station of departure from statistics
     *
     * @param statistics received statistics
     * @return pair of station name and the number of departing passengers from it
     */
    private static Map.Entry<String, Integer> getBestDeparture(ArrayList<Statistics> statistics) {

        Map<String, Integer> rating = new TreeMap<String, Integer>();
        for (Statistics s : statistics) {
            String departureStation = s.getDepartureStation();
            if (rating.containsKey(departureStation)) { // if we have already counted this station - lets increment his counter
                rating.put(departureStation, (rating.get(departureStation) +1));
            }
            else { // new entry, this station has just one passenger
                rating.put(departureStation, 1);
            }
        }

        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : rating.entrySet()) // looking for most occupied station
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        return maxEntry; // pair of station name and the number of departing passengers from it
    }

    /**
     * Method to get most wanted station of arrival from statistics
     *
     * @param statistics received statistics
     * @return pair of station name and the number of arriving passengers on it
     */
    private static Map.Entry<String, Integer> getBestArrival(ArrayList<Statistics> statistics) {

        Map<String, Integer> rating = new TreeMap<String, Integer>();
        for (Statistics s : statistics) {
            String arrivalStation = s.getArrivalStation();
            if (rating.containsKey(arrivalStation)) { // if we have already counted this station - lets increment his counter
                rating.put(arrivalStation, (rating.get(arrivalStation) +1));
            }
            else { // new entry, this station has just one passenger
                rating.put(arrivalStation, 1);
            }
        }

        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : rating.entrySet()) // looking for most occupied station
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        return maxEntry; // pair of station name and the number of arriving passengers on it
    }

}