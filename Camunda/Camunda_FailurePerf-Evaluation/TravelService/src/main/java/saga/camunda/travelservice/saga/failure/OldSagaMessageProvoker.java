package saga.camunda.travelservice.saga.failure;

import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class OldSagaMessageProvoker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OldSagaMessageProvoker.class);

    private final String camundaServerUri;

    private final ObjectValue typedRequest;

    public OldSagaMessageProvoker(final String camundaServerUri, final ObjectValue typedRequest) {
        this.camundaServerUri = camundaServerUri;
        this.typedRequest = typedRequest;
    }

    @Override
    public void run() {
        logger.info("Started OldSagaMessageProvoker thread and preparing the request.");

        String startProcessInstanceEndpoint = "/process-definition/key/BookTripSaga/start";

        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ObjectValue> request = new HttpEntity<>(typedRequest, requestHeader);

        try { // TODO Alternative idea: wait for condition that will become true while confirming the TripTask
            logger.info("Wait for 5 minutes before sending the Saga start request again.");
            // wait for 5 minutes so that the message will definitely be an old one
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            // ignore
        }

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(camundaServerUri + startProcessInstanceEndpoint, request, String.class);
        logger.info("Sent request and received response: " + response);
    }
}
