package saga.netflix.conductor.flightservice.controller.worker;

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
import saga.netflix.conductor.flightservice.controller.IFlightService;
import saga.netflix.conductor.flightservice.error.ErrorMessage;
import saga.netflix.conductor.flightservice.error.ErrorType;
import saga.netflix.conductor.flightservice.error.FlightServiceException;
import saga.netflix.conductor.flightservice.model.FindAndBookFlightInformation;
import saga.netflix.conductor.flightservice.model.FlightInformation;
import saga.netflix.conductor.flightservice.resources.DtoConverter;
import saga.netlfix.conductor.flightservice.api.FlightServiceTasks;
import saga.netlfix.conductor.flightservice.api.dto.BookFlightResponse;
import saga.netlfix.conductor.flightservice.api.dto.BookFlightRequest;

import java.io.IOException;
import java.util.Map;

public class BookFlightWorker implements Worker {

    private static final Logger logger = LoggerFactory.getLogger(BookFlightWorker.class);

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final IFlightService flightService;

    @Autowired
    private final DtoConverter dtoConverter;

    private final String inputBookFlight = FlightServiceTasks.TaskInput.BOOK_FLIGHT_INPUT;

    public BookFlightWorker(final ObjectMapper objectMapper, final IFlightService flightService,
                            final DtoConverter dtoConverter) {
        this.objectMapper = objectMapper;
        this.flightService = flightService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public String getTaskDefName() {
        return FlightServiceTasks.Task.BOOK_FLIGHT;
    }

    @Override
    public TaskResult execute(final Task task) {
        logger.info("Start execution of " + getTaskDefName());

        final TaskResult taskResult = new TaskResult(task);

        Map<String, Object> taskInput = task.getInputData();
        if (taskInput == null || !taskInput.containsKey(inputBookFlight)) {
            String errorMessage = String.format("%s: misses the necessary input data (%s)", getTaskDefName(),
                    inputBookFlight);
            logger.info(errorMessage);
            taskResult.setReasonForIncompletion(new ErrorMessage(ErrorType.INTERNAL_ERROR, errorMessage).toString());
            // prevent retry --> input would still be missing, so no reason to retry
            taskResult.setStatus(TaskResult.Status.FAILED_WITH_TERMINAL_ERROR);
            return taskResult;
        }

        try {
            bookFlight(taskInput, taskResult);
            taskResult.setStatus(TaskResult.Status.COMPLETED);
        } catch (FlightServiceException exception) {
            logger.error(exception.toString());
            taskResult.setReasonForIncompletion(exception.toString());
            // prevent retry --> flights will still be fully booked
            taskResult.setStatus(TaskResult.Status.FAILED_WITH_TERMINAL_ERROR);
        }

        return taskResult;
    }

    private void bookFlight(final Map<String, Object> taskInput, final TaskResult taskResult) throws FlightServiceException {
        logger.info("TaskInput: " + taskInput.get(inputBookFlight));
        final BookFlightRequest bookFlightRequest = objectMapper.convertValue(taskInput.get(inputBookFlight),
                BookFlightRequest.class);

        FindAndBookFlightInformation flightInformation =
                dtoConverter.convertToFindAndBookFlightInformation(bookFlightRequest);
        FlightInformation receivedFlightInformation = flightService.findAndBookFlight(flightInformation);

        BookFlightResponse bookFlightResponse = new BookFlightResponse(bookFlightRequest.getTripId(),
                receivedFlightInformation.getId(),
                receivedFlightInformation.getBookingStatus().toString());

        taskResult.getOutputData().put(FlightServiceTasks.TaskOutput.BOOK_FLIGHT_OUTPUT, bookFlightResponse);
        logger.info("Flight successfully booked: " + bookFlightResponse);

        // provoke Orchestrator failure (Conductor Server)
        provokeOrchestratorFailure(flightInformation.getDestination().getCountry(),
                receivedFlightInformation.getProvokedFailure());

        // provoke own failure if it has not already been done before for this flight booking
        provokeOwnFailure(flightInformation.getDestination().getCountry(),
                receivedFlightInformation.getProvokedFailure());
    }

    private void provokeOrchestratorFailure(String failureInput, boolean alreadyBeenProvoked) {
        if (!failureInput.equalsIgnoreCase("Provoke orchestrator failure while executing") || alreadyBeenProvoked) {
            return;
        }

        logger.info("Shutting down ConductorServer due to corresponding input.");
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
        dockerClient.stopContainerCmd("conductor-server-ui").exec();
        try {
            dockerClient.close();
        } catch (IOException e) {
            logger.warn("Docker client could not be closed", e);
        }
    }

    private void provokeOwnFailure(String failureInput, boolean alreadyBeenProvoked) {
        if (alreadyBeenProvoked) {
            return;
        }

        if (failureInput.equalsIgnoreCase("Provoke exception while executing")) {
            logger.info("Throwing runtime exception due to corresponding input.");
            throw new RuntimeException("Test participant behaviour when provoking exception while executing");
        }

        if (failureInput.equalsIgnoreCase("Provoke participant failure while executing")) {
            logger.info("Shutting down FlightService due to corresponding input.");
            // Force the JVM to terminate to simulate sudden failure
            Runtime.getRuntime().halt(1);
        }
    }

}
