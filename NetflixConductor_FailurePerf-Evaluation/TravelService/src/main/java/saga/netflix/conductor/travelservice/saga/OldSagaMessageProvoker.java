package saga.netflix.conductor.travelservice.saga;

import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OldSagaMessageProvoker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OldSagaMessageProvoker.class);

    private final String conductorServerUri;

    private final StartWorkflowRequest bookTripSagaRequest;

    public OldSagaMessageProvoker(final String conductorServerUri, final StartWorkflowRequest bookTripSagaRequest) {
        this.conductorServerUri = conductorServerUri;
        this.bookTripSagaRequest = bookTripSagaRequest;
    }

    @Override
    public void run() {
        logger.info("Started OldSagaMessageProvoker thread and preparing the request.");

        WorkflowClient workflowClient = new WorkflowClient();
        workflowClient.setRootURI(conductorServerUri);

        try { // TODO Alternative idea: wait for condition that will become true while confirming the TripTask
            logger.info("Wait for 5 minutes before sending the Saga start request again.");
            // wait for 5 minutes so that the message will definitely be an old one
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            // ignore
        }

        workflowClient.startWorkflow(bookTripSagaRequest);
        logger.info("Sent start Saga request again.");
    }
}
