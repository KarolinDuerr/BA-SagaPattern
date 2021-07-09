package saga.camunda.hotelservice.controller.worker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import saga.camunda.hotelservice.api.HotelServiceTopics;
import saga.camunda.hotelservice.api.dto.BookHotelRequest;
import saga.camunda.hotelservice.api.dto.BookHotelResponse;
import saga.camunda.hotelservice.controller.IHotelService;
import saga.camunda.hotelservice.controller.worker.failure.CompleteTaskRequest;
import saga.camunda.hotelservice.controller.worker.failure.OldMessageProvoker;
import saga.camunda.hotelservice.error.HotelServiceException;
import saga.camunda.hotelservice.model.HotelBooking;
import saga.camunda.hotelservice.model.HotelBookingInformation;
import saga.camunda.hotelservice.resources.DtoConverter;
import saga.camunda.travelservice.api.TravelServiceTopics;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription(value = "bookHotel", processDefinitionKey = TravelServiceTopics.Sagas.BOOK_TRIP_SAGA, lockDuration = 30000)
public class BookHotelWorker implements ExternalTaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(BookHotelWorker.class);

    @Autowired
    private final IHotelService hotelService;

    @Autowired
    private final DtoConverter dtoConverter;

    @Value("${camunda.bpm.client.base-url}")
    private String camundaServerUri;

    public BookHotelWorker(final IHotelService hotelService, final DtoConverter dtoConverter) {
        this.hotelService = hotelService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        logger.info("Start execution of: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");

        BookHotelRequest bookHotelRequest = externalTask.getVariable(HotelServiceTopics.DataInput.BOOK_HOTEL_DATA);

        if (bookHotelRequest == null) {
            logger.info("The given input could not be parsed to a bookHotelRequest.");
            externalTaskService.handleBpmnError(externalTask, HotelServiceTopics.BpmnError.HOTEL_ERROR, "Something " +
                    "went" +
                    " wrong with the given input.");
            return;
        }

        try {
            bookHotel(bookHotelRequest, externalTask, externalTaskService);
        } catch (HotelServiceException exception) {
            logger.error(exception.toString());
            externalTaskService.handleBpmnError(externalTask, HotelServiceTopics.BpmnError.HOTEL_ERROR,
                    exception.toString());
        }

        logger.debug("Finished Task: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");
    }

    private void bookHotel(final BookHotelRequest bookHotelRequest, final ExternalTask externalTask,
                           final ExternalTaskService externalTaskService) throws HotelServiceException {
        HotelBookingInformation bookingInformation =
                dtoConverter.convertToHotelBookingInformation(bookHotelRequest);

        HotelBooking hotelBooking = hotelService.bookHotel(bookHotelRequest.getTravellerName(), bookingInformation);

        BookHotelResponse bookingResponse = new BookHotelResponse(bookHotelRequest.getTripId(), hotelBooking.getId(),
                hotelBooking.getHotelName(), hotelBooking.getBookingStatus().toString());

        ObjectValue typedBookHotelResponse =
                Variables.objectValue(bookingResponse).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();
        Map<String, Object> variables = new HashMap<>();
        variables.put(HotelServiceTopics.DataOutput.BOOK_HOTEL_RESPONSE, typedBookHotelResponse);

        // provoke different failure scenarios
        provokeFailures(bookingInformation.getDestination().getCountry(), externalTaskService, externalTask, variables);

        externalTaskService.complete(externalTask, variables);
        logger.info("Hotel successfully booked: " + bookingResponse);
    }

    private void provokeFailures(final String failureInput, final ExternalTaskService externalTaskService,
                                 final ExternalTask externalTask, final Map<String, Object> variables) {
        // provoke Participant failure (FlightService)
        provokeParticipantFailure(failureInput);

        // provoke duplicate message --> acknowledge task twice
        provokeDuplicateMessageToOrchestrator(failureInput, externalTaskService, externalTask, variables);

        // provoke sending an old message to the orchestrator
        provokeOldMessageToOrchestrator(failureInput, externalTask, variables);
    }

    private void provokeParticipantFailure(String failureInput) {
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
        dockerClient.stopContainerCmd("flightservice_camundaFailurePerf").exec();
        try {
            dockerClient.close();
        } catch (IOException e) {
            logger.warn("Docker client could not be closed", e);
        }
    }

    private void provokeDuplicateMessageToOrchestrator(final String failureInput,
                                                       final ExternalTaskService externalTaskService,
                                                       final ExternalTask externalTask,
                                                       final Map<String, Object> variables) {
        if (!failureInput.equalsIgnoreCase("Provoke duplicate message to orchestrator")) {
            return;
        }

        logger.info("Provoking immediate duplicate message to orchestrator.");
        externalTaskService.complete(externalTask, variables);
    }

    private void provokeOldMessageToOrchestrator(final String failureInput, final ExternalTask externalTask,
                                                 final Map<String, Object> variables) {
        if (!failureInput.equalsIgnoreCase("Provoke sending old message to orchestrator")) {
            return;
        }

        logger.info("Provoking delayed duplicate message to orchestrator --> old message");

        CompleteTaskRequest completeTaskRequest = new CompleteTaskRequest(externalTask.getWorkerId(), variables);
        ObjectValue typedRequest =
                Variables.objectValue(completeTaskRequest).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();

        OldMessageProvoker oldMessageProvoker = new OldMessageProvoker(camundaServerUri, typedRequest,
                externalTask.getId());
        new Thread(oldMessageProvoker).start();
    }
}
