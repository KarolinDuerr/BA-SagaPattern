package saga.netflix.conductor.hotelservice.controller.worker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.hotelservice.api.dto.BookHotelRequest;
import saga.netflix.conductor.hotelservice.api.dto.BookHotelResponse;
import saga.netflix.conductor.hotelservice.controller.IHotelService;
import saga.netflix.conductor.hotelservice.error.ErrorMessage;
import saga.netflix.conductor.hotelservice.error.ErrorType;
import saga.netflix.conductor.hotelservice.error.HotelServiceException;
import saga.netflix.conductor.hotelservice.model.HotelBooking;
import saga.netflix.conductor.hotelservice.model.HotelBookingInformation;
import saga.netflix.conductor.hotelservice.resources.DtoConverter;

import java.io.IOException;
import java.util.Map;

public class BookHotelWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(BookHotelWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final IHotelService hotelService;

    @Autowired
    private final DtoConverter dtoConverter;

    private final String inputBookHotel = HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT;

    @Value("${CONDUCTOR_SERVER_URI:conductor.server.uri}")
    private String conductorServerUri;

    public BookHotelWorker(final ObjectMapper objectMapper, final IHotelService hotelService,
                           final DtoConverter dtoConverter) {
        this.objectMapper = objectMapper;
        this.hotelService = hotelService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public String getTaskDefName() {
        return HotelServiceTasks.Task.BOOK_HOTEL;
    }

    @Override
    public TaskResult execute(final Task task) {
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);

        Map<String, Object> taskInput = task.getInputData();
        if (taskInput == null || !taskInput.containsKey(inputBookHotel)) {
            String errorMessage = String.format("%s: misses the necessary input data (%s)", getTaskDefName(),
                    inputBookHotel);
            logger.info(errorMessage);
            taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, errorMessage).toString());
            // prevent retry --> input would still be missing, so no reason to retry
            taskResult.setStatus(TaskResult.Status.FAILED_WITH_TERMINAL_ERROR);
            return taskResult;
        }

        try {
            bookHotel(taskInput, taskResult);
            taskResult.setStatus(TaskResult.Status.COMPLETED);
        } catch (HotelServiceException exception) {
            logger.error(exception.toString());
            taskResult.setReasonForIncompletion(exception.toString());
            // prevent retry --> hotels will still be fully booked
            taskResult.setStatus(TaskResult.Status.FAILED_WITH_TERMINAL_ERROR);
        }

        return taskResult;
    }

    private void bookHotel(final Map<String, Object> taskInput, final TaskResult taskResult) throws HotelServiceException {
        logger.info("TaskInput: " + taskInput.get(inputBookHotel));
        final BookHotelRequest bookHotelRequest = objectMapper.convertValue(taskInput.get(inputBookHotel),
                BookHotelRequest.class);

        HotelBookingInformation bookingInformation =
                dtoConverter.convertToHotelBookingInformation(bookHotelRequest);

        HotelBooking hotelBooking = hotelService.bookHotel(bookHotelRequest.getTravellerName(), bookingInformation);

        BookHotelResponse bookingResponse = new BookHotelResponse(bookHotelRequest.getTripId(), hotelBooking.getId(),
                hotelBooking.getHotelName(), hotelBooking.getBookingStatus().toString());

        taskResult.getOutputData().put(HotelServiceTasks.TaskOutput.BOOK_HOTEL_OUTPUT, bookingResponse);
        logger.info("Hotel successfully booked: " + bookingResponse);

        // provoke different failure scenarios
        provokeFailures(bookingInformation.getDestination().getCountry(), taskResult);
    }

    private void provokeFailures(final String failureInput, final TaskResult taskResult) {
        // provoke Participant failure (FlightService)
        provokeParticipantFailure(failureInput);

        // provoke duplicate message --> acknowledge task twice
        provokeDuplicateMessageToOrchestrator(failureInput, taskResult);

        // provoke sending an old message to the orchestrator
        provokeOldMessageToOrchestrator(failureInput, taskResult);
    }

    private void provokeParticipantFailure(final String failureInput) {
        if (!failureInput.equalsIgnoreCase("Provoke participant failure before receiving task")) {
            return;
        }

        logger.info("Shutting down FlightService due to corresponding input.");
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")
                .withDockerTlsVerify(false)
                .withDockerCertPath("/home/user/.docker/certs")
                .withDockerConfig("/home/user/.docker")
                .build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();
        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        dockerClient.stopContainerCmd("flightservice_conductorFailurePerf").exec();
        try {
            dockerClient.close();
        } catch (IOException e) {
            logger.warn("Docker client could not be closed", e);
        }
    }

    // TODO check --> also using a Thread?
    private void provokeDuplicateMessageToOrchestrator(final String failureInput, final TaskResult taskResult) {
        if (!failureInput.equalsIgnoreCase("Provoke duplicate message to orchestrator")) {
            return;
        }

        String updateTaskEndpoint = "/tasks";
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.setContentType(MediaType.APPLICATION_JSON);
        taskResult.setStatus(TaskResult.Status.COMPLETED);
        final JsonNode result =
                objectMapper.valueToTree(taskResult);

        HttpEntity<JsonNode> request = new HttpEntity<>(result, requestHeader);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(conductorServerUri + updateTaskEndpoint, request, Void.class);
    }

    // TODO check
    private void provokeOldMessageToOrchestrator(final String failureInput, final TaskResult taskResult) {
        if (!failureInput.equalsIgnoreCase("Provoke sending old message to orchestrator")) {
            return;
        }

        taskResult.setStatus(TaskResult.Status.COMPLETED);
        final JsonNode result =
                objectMapper.valueToTree(taskResult);

        OldMessageProvoker oldMessageProvoker = new OldMessageProvoker(conductorServerUri, result);
        new Thread(oldMessageProvoker).start();
    }
}
