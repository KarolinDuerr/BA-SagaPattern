package saga.camunda.flightservice.controller.worker;

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
import saga.camunda.flightservice.api.FlightServiceTopics;
import saga.camunda.flightservice.api.dto.BookFlightRequest;
import saga.camunda.flightservice.api.dto.BookFlightResponse;
import saga.camunda.flightservice.controller.IFlightService;
import saga.camunda.flightservice.error.ErrorType;
import saga.camunda.flightservice.error.FlightServiceException;
import saga.camunda.flightservice.model.FindAndBookFlightInformation;
import saga.camunda.flightservice.model.FlightInformation;
import saga.camunda.flightservice.resources.DtoConverter;

import java.util.HashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription("bookFlight")
public class BookFlightWorker implements ExternalTaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(BookFlightWorker.class);

    @Autowired
    private final IFlightService flightService;

    @Autowired
    private final DtoConverter dtoConverter;

    public BookFlightWorker(final IFlightService flightService, final DtoConverter dtoConverter) {
        this.flightService = flightService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        logger.info("Start execution of: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");

        BookFlightRequest bookFlightRequest = externalTask.getVariable(FlightServiceTopics.DataInput.BOOK_FLIGHT_DATA);

        if (bookFlightRequest == null) {
            logger.info("The given input could not be parsed to a bookFlightRequest.");
            externalTaskService.handleBpmnError(externalTask, ErrorType.INVALID_PARAMETER.toString(), "Something went" +
                    " wrong with the given input."); // TODO
            return;
        }

        try {
            bookFlight(bookFlightRequest, externalTask, externalTaskService);
        } catch (FlightServiceException exception) {
            logger.error(exception.toString());
            externalTaskService.handleBpmnError(externalTask, exception.getErrorType().toString(),
                    exception.getMessage()); // TODO
        }
        logger.info("Finished Task: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");
    }

    private void bookFlight(final BookFlightRequest bookFlightRequest, final ExternalTask externalTask,
                            final ExternalTaskService externalTaskService) throws FlightServiceException {
        FindAndBookFlightInformation flightInformation =
                dtoConverter.convertToFindAndBookFlightInformation(bookFlightRequest);
        FlightInformation receivedFlightInformation = flightService.findAndBookFlight(flightInformation);

        BookFlightResponse bookFlightResponse = new BookFlightResponse(bookFlightRequest.getTripId(),
                receivedFlightInformation.getId(),
                receivedFlightInformation.getBookingStatus().toString());

        ObjectValue typedBookFlightResponse =
                Variables.objectValue(bookFlightResponse).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();
        Map<String, Object> variables = new HashMap<>();
        variables.put(FlightServiceTopics.DataOutput.BOOK_FLIGHT_RESPONSE, typedBookFlightResponse);

        externalTaskService.complete(externalTask, variables);
        logger.info("Flight successfully booked: " + bookFlightResponse);
    }
}
