package raiload.statistics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * EJB Bean
 * used to get Statistics from Spring REST service
 *
 * @author vbuevich
 */
@Stateless
public class StatisticsEJB {

      private static final Logger LOGGER = Logger.getLogger(PdfStatisticsView.class);

      /**
       * Method of EJB to get list of Statistics from REST Service
       *
       * @param startTime start period for statistics
       * @param endTime end period for statistics
       * @return list of statistics
       */
      public static ArrayList<Statistics> getStatistics(String startTime, String endTime) {

            Client client = Client.create(); // REST client
            WebResource webResource = client.resource("http://localhost:8080/railroad/statistics"); // our our Spring REST service
            ObjectMapper mapper = new ObjectMapper(); // for JSon

            MultivaluedMap<String, String> params = new MultivaluedMapImpl(); // parameters
            params.add("startTime", startTime);
            params.add("endTime", endTime);

            ArrayList<Statistics> statisticsList = null; // to get Statistics form REST service

            try {
                  ClientResponse response = webResource
                          .queryParams(params) // 2 params, startTime and endTime
                          .accept(MediaType.APPLICATION_JSON)
                          .type(MediaType.APPLICATION_JSON)
                          .post(ClientResponse.class, "");

                  if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                  }
                  String output = response.getEntity(String.class); // JSon here
                  statisticsList = mapper.readValue(output, new TypeReference<List<Statistics>>(){}); // we mapping JSon as List<Statistics>

            }
            catch (IOException e) {
                  LOGGER.error(e.getMessage());
            }
            return statisticsList;
      }
}
