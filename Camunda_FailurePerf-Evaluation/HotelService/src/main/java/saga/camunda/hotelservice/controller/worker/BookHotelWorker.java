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
import org.springframework.stereotype.Component;
import saga.camunda.hotelservice.api.HotelServiceTopics;
import saga.camunda.hotelservice.api.dto.BookHotelRequest;
import saga.camunda.hotelservice.api.dto.BookHotelResponse;
import saga.camunda.hotelservice.controller.IHotelService;
import saga.camunda.hotelservice.error.HotelServiceException;
import saga.camunda.hotelservice.model.HotelBooking;
import saga.camunda.hotelservice.model.HotelBookingInformation;
import saga.camunda.hotelservice.resources.DtoConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription("bookHotel")
public class BookHotelWorker implements ExternalTaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(BookHotelWorker.class);

    @Autowired
    private final IHotelService hotelService;

    @Autowired
    private final DtoConverter dtoConverter;

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
            externalTaskService.handleBpmnError(externalTask, HotelServiceTopics.BpmnError.HOTEL_ERROR, "Something went" +
                    " wrong with the given input.");
            externalTaskService.complete(externalTask, null);
            return;
        }

        try {
            bookHotel(bookHotelRequest, externalTask, externalTaskService);
        } catch (HotelServiceException exception) {
            logger.error(exception.toString());
            externalTaskService.handleBpmnError(externalTask, HotelServiceTopics.BpmnError.HOTEL_ERROR,
                    exception.toString());
            externalTaskService.complete(externalTask, null);
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

        // provoke Participant failure (FlightService)
        provokeParticipantFailure(bookingInformation.getDestination().getCountry());

        externalTaskService.complete(externalTask, variables);
        logger.info("Hotel successfully booked: " + bookingResponse);
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
}
