package raiload.statistics;

//import com.sun.jersey.api.Client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * @author vbuevich
 */
@Stateless
public class StatisticsEJB {

      public static ArrayList<Statistics> getStatistics(String startTime, String endTime) {

            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8080/JPA-Hibernate-1.0-SNAPSHOT/statistics");
            ObjectMapper mapper = new ObjectMapper();

            MultivaluedMap<String, String> params = new MultivaluedMapImpl();
            params.add("startTime", startTime);
            params.add("endTime", endTime);

            try {
                  // String JSONRequest = mapper.writeValueAsString(request);
                  String JSONRequest = "";
                  ClientResponse response = webResource
                          .queryParams(params)
                          .accept(MediaType.APPLICATION_JSON)
                          .type(MediaType.APPLICATION_JSON)
                          .post(ClientResponse.class, "");

                  if (response.getStatus() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                  }
                  String output = response.getEntity(String.class);
                  ArrayList<Statistics> statisticsList = mapper.readValue(output, new TypeReference<List<Statistics>>(){});

                  return statisticsList;
            }
            catch (IOException e) {
                 throw new RuntimeException(e.getMessage());
            }


      }

}
