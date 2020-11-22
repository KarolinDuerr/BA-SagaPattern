package saga.netflix.conductor.travelservice.saga.bookTripSaga;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Create and register the task and workflow definitions for the BookTripSaga and its compensating workflow.
 */
public class BookTripSaga {

    private static final Logger logger = LoggerFactory.getLogger(BookTripSaga.class);

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private final ObjectMapper objectMapper;

    private final String conductorServerUri;

    private final HttpHeaders requestHeader;

    private final List<JsonNode> taskDefinitions;

    private final static String TASK_DEFINITION_ENDPOINT = "metadata/taskdefs";
    private final static String WORKFLOW_DEFINITION_ENDPOINT = "metadata/workflow";

    public BookTripSaga(final String conductorServerUri, final RestTemplate restTemplate,
                        final ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;

        this.conductorServerUri = conductorServerUri;
        requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        this.taskDefinitions = new LinkedList<>();
    }

    public void registerWorkflowAndTasks() {
        getTaskDefinitions();
        registerTasks();
        registerWorkflows();
    }

    private void getTaskDefinitions() {
        try {
            final JsonNode bookHotelTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/bookHotel.json")));
            final JsonNode cancelHotelTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/cancelHotel.json")));
            final JsonNode bookFlightTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/bookFlight.json")));
            final JsonNode cancelFlightTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/cancelFlight.json")));
            final JsonNode confirmHotelTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/confirmHotel.json")));
            final JsonNode confirmTripTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/confirmTrip.json")));
            final JsonNode rejectTripTask =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/taskDefinitions/rejectTrip.json")));

            taskDefinitions.add(bookHotelTask);
            taskDefinitions.add(cancelHotelTask);
            taskDefinitions.add(bookFlightTask);
            taskDefinitions.add(cancelFlightTask);
            taskDefinitions.add(confirmHotelTask);
            taskDefinitions.add(confirmTripTask);
            taskDefinitions.add(rejectTripTask);
        } catch (IOException e) {
            logger.error("exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void registerTasks() {
        HttpEntity<List<JsonNode>> request = new HttpEntity<>(taskDefinitions, requestHeader);
        restTemplate.postForObject(conductorServerUri + TASK_DEFINITION_ENDPOINT, request, Void.class);
        logger.info("Registered task definitions of the BookTripSaga");
    }

    private void registerWorkflows() {
        try {
            final JsonNode bookTripDefinition =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/workflows/bookTripSaga.json")));
            final JsonNode cancelTripDefinition =
                    objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/workflows/compensateBookTripSaga.json")));

            registerWorkflow(bookTripDefinition);
            registerWorkflow(cancelTripDefinition);
        } catch (IOException e) {
            logger.error("exception " + e.getMessage());
            e.printStackTrace();
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
        }
        logger.info(String.format("Workflow definition (%s) trying to register.", workflowToRegister.get("name")));
    }
}
