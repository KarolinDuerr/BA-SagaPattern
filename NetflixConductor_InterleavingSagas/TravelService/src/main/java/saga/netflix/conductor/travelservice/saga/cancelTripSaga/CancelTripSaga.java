package saga.netflix.conductor.travelservice.saga.cancelTripSaga;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Create and register the task and workflow definitions for the CancelTripSaga and its compensating workflow.
 */
public class CancelTripSaga {

    private static final Logger logger = LoggerFactory.getLogger(CancelTripSaga.class);

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private final ObjectMapper objectMapper;

    private final String conductorServerUri;

    private final HttpHeaders requestHeader;

    private final List<JsonNode> taskDefinitions;

    private final static String TASK_DEFINITION_ENDPOINT = "metadata/taskdefs/cancelTripSaga";
    private final static String WORKFLOW_DEFINITION_ENDPOINT = "metadata/workflow";

    public CancelTripSaga(final String conductorServerUri, final RestTemplate restTemplate,
                          final ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;

        this.conductorServerUri = conductorServerUri;
        requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        this.taskDefinitions = new LinkedList<>();
    }

    public void registerWorkflowAndTasks() {
        // Server tasks a bit till its up and running --> wait so that registering is possible
        waitForServerToBeAvailable();

        getTaskDefinitions();
        registerTasks();
        registerWorkflows();
    }

    private void getTaskDefinitions() {
        try {
            final JsonNode cancelHotelTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/cancelTripSaga/cancelHotel.json")));
            final JsonNode rebookHotelTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/cancelTripSaga/rebookHotel.json")));
            final JsonNode cancelFlightTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/cancelTripSaga/cancelFlight.json")));
            final JsonNode rebookFlightTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/cancelTripSaga/rebookFlight.json")));
            final JsonNode confirmHotelCancellationTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/cancelTripSaga/confirmHotelCancellation.json")));
            final JsonNode confirmTripCancellationTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/cancelTripSaga/confirmTripCancellation.json")));
            final JsonNode rejectTripCancellationTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/cancelTripSaga/rejectTripCancellation.json")));

            taskDefinitions.add(cancelHotelTask);
            taskDefinitions.add(rebookHotelTask);
            taskDefinitions.add(cancelFlightTask);
            taskDefinitions.add(rebookFlightTask);
            taskDefinitions.add(confirmHotelCancellationTask);
            taskDefinitions.add(confirmTripCancellationTask);
            taskDefinitions.add(rejectTripCancellationTask);
        } catch (IOException exception) {
            logger.error("Exception while reading task blueprints: " + exception.getMessage() + ", with cause: " + exception.getCause());
        }
    }

    private void registerTasks() {
        HttpEntity<List<JsonNode>> request = new HttpEntity<>(taskDefinitions, requestHeader);
        try {
            restTemplate.postForObject(conductorServerUri + TASK_DEFINITION_ENDPOINT, request, Void.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.CONFLICT && exception.getMessage().contains("already exists!")) {
                // same task has already been registered
                return;
            }
            String errorMessage = String.format("Exception occurred while registering task definitions: %s, with " +
                    "cause %s", exception.getMessage(), exception.getCause());
            logger.error(errorMessage);
        }
        logger.info("Registered task definitions of the CancelTripSaga");
    }

    private void registerWorkflows() {
        try {
            final JsonNode bookTripDefinition =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/workflows/bookTripSaga.json")));
            final JsonNode cancelTripDefinition =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/workflows/compensateBookTripSaga.json")));

            registerWorkflow(bookTripDefinition);
            registerWorkflow(cancelTripDefinition);
        } catch (IOException exception) {
            logger.error("Exception while reading workflow blueprints: " + exception.getMessage() + ", with cause: " + exception.getCause());
        }
    }

    private void registerWorkflow(JsonNode workflowToRegister) {
        HttpEntity<JsonNode> request = new HttpEntity<>(workflowToRegister, requestHeader);
        try {
            restTemplate.postForObject(conductorServerUri + WORKFLOW_DEFINITION_ENDPOINT, request, Void.class);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.CONFLICT && exception.getMessage().contains("already exists!")) {
                // same workflow has already been registered
                return;
            }
            String errorMessage = String.format("Exception occurred while registering workflow definition '%s': %s, " +
                    "with cause %s", workflowToRegister.get("name"), exception.getMessage(), exception.getCause());
            logger.error(errorMessage);
        }
        logger.info(String.format("Workflow definition (%s) trying to register.", workflowToRegister.get("name")));
    }

    private void waitForServerToBeAvailable() {
        boolean serverUp = false;
        while (!serverUp) {
            try {
                ResponseEntity<JsonNode> responseEntity = restTemplate.getForEntity(conductorServerUri + "/health",
                        JsonNode.class);
                serverUp = true;
                String message = responseEntity == null ? "null" :
                        responseEntity.getStatusCode() + " " + responseEntity.getBody();
                logger.info("Response: " + message);
            } catch (ResourceAccessException exception) {
                // server not yet reachable
                logger.info("Occurred exception: " + exception.getMessage() + ", with cause: " + exception.getCause());
                logger.info("Server not available yet. Retrying in 50s");
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }
}
