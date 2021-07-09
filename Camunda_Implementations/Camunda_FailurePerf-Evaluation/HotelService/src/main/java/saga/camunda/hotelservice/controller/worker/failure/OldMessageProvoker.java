package saga.camunda.hotelservice.controller.worker.failure;

import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class OldMessageProvoker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(OldMessageProvoker.class);

    private final String camundaServerUri;

    private final ObjectValue typedRequest;

    private final String taskId;

    public OldMessageProvoker(final String camundaServerUri, final ObjectValue typedRequest, final String taskId) {
        this.camundaServerUri = camundaServerUri;
        this.typedRequest = typedRequest;
        this.taskId = taskId;
    }

    @Override
    public void run() {
        logger.info("Started OldMessageProvoker thread and preparing the request.");

        String updateTaskEndpoint = String.format("/external-task/%s/complete", taskId);

        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ObjectValue> requestVariables = new HttpEntity<>(typedRequest, requestHeader);

        try {
            logger.info("Wait for 5 minutes before sending the request again.");
            // wait for 5 minutes so that the message will definitely be an old one
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            // ignore
        }

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(camundaServerUri + updateTaskEndpoint, requestVariables, String.class);
        logger.info("Sent request and received response: " + response);
    }
}
