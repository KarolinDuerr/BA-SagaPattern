package saga.microprofile.travelservice.saga;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class CancelLRARunnable implements Runnable {

    private static final Logger logger = Logger.getLogger(CancelLRARunnable.class.toString());

    private final String lraId;

    public CancelLRARunnable(final String lraId) {
        this.lraId = lraId;
    }

    @Override
    public void run() {
        try {
            logger.info("Starting to cancel LRA thread --> Wait for 15 seconds before sending the request, so the ID " +
                    "exists.");
            // wait for 15 seconds before trying to close the active LRA since it has just been created
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            // ignore
        }

        Client coordinatorClient = ClientBuilder.newClient();
        String closeLraUri = String.format("%s/cancel", lraId);
        logger.info("Uri: " + closeLraUri);
        WebTarget coordinatorTarget = coordinatorClient.target(closeLraUri);
        Response coordinatorResponse = coordinatorTarget.request().put(null);
        logger.info("Coordinator Cancel Response: " + (coordinatorResponse == null ? "null" :
                coordinatorResponse.getStatus()));
        coordinatorClient.close();
    }
}
