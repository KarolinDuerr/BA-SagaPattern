package saga.netflix.conductor.hotelservice.controller.worker;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class OldMessageProvoker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OldMessageProvoker.class);

    private final String conductorServerUri;

    private final JsonNode result;

    public OldMessageProvoker(final String conductorServerUri, final JsonNode result) {
        this.conductorServerUri = conductorServerUri;
        this.result = result;
    }

    @Override
    public void run() {
        logger.info("Started OldMessageProvoker thread and preparing the request.");

        String updateTaskEndpoint = "/tasks";

        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JsonNode> request = new HttpEntity<>(result, requestHeader);

        try { // TODO Alternative idea: wait for conidition that will become true while confirming the HotelTask
            logger.info("Wait for 5 minutes before sending the request again.");
            // wait for 5 minutes so that the message will definitely be an old one
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            // ignore
        }

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(conductorServerUri + updateTaskEndpoint, request, String.class);
        logger.info("Sent request and received response: " + response);
    }
}
