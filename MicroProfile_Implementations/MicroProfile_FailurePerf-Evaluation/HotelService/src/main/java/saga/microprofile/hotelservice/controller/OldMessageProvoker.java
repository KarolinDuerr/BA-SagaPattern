package saga.microprofile.hotelservice.controller;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.logging.Logger;

public class OldMessageProvoker implements Runnable {

    private static final java.util.logging.Logger logger = Logger.getLogger(OldMessageProvoker.class.toString());

    private final String lraCoordinatorUri;

    private final URI lraId;

    public OldMessageProvoker(final String lraCoordinatorUri, final URI lraId) {
        this.lraCoordinatorUri = lraCoordinatorUri;
        this.lraId = lraId;
    }

    @Override
    public void run() {
        logger.info("Started OldMessageProvoker thread and preparing the request.");

        String closeLraEndpoint = "/close";

        String lraCloseUri = String.format("%s/%s/%s", lraCoordinatorUri, lraId, closeLraEndpoint);
        Client travelServiceClient = ClientBuilder.newClient();
        WebTarget travelServiceTarget = travelServiceClient.target(lraCloseUri);

        try {
            logger.info("Wait for 5 minutes before sending the request again.");
            // wait for 5 minutes so that the message will definitely be an old one
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            // ignore
        }

        Response closeLraResponse = travelServiceTarget.request().put(null);
        logger.info("Received TravelService close response: " + closeLraResponse.getStatus());
    }
}
