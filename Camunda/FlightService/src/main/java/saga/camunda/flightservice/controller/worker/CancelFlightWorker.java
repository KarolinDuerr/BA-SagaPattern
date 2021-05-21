package saga.camunda.flightservice.controller.worker;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.camunda.flightservice.api.FlightServiceTopics;
import saga.camunda.flightservice.api.dto.BookFlightRequest;
import saga.camunda.flightservice.controller.IFlightService;
import saga.camunda.flightservice.error.ErrorType;


@Component
@ExternalTaskSubscription("cancelFlight")
public class CancelFlightWorker implements ExternalTaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(CancelFlightWorker.class);

    @Autowired
    private final IFlightService flightService;

    public CancelFlightWorker(final IFlightService flightService) {
        this.flightService = flightService;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        logger.info("Start execution of: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");

        BookFlightRequest bookFlightRequest = externalTask.getVariable(FlightServiceTopics.DataInput.BOOK_FLIGHT_DATA);

        if (bookFlightRequest == null) {
            logger.info("The given input could not be parsed to a bookHotelRequest.");
            externalTaskService.handleBpmnError(externalTask, ErrorType.INVALID_PARAMETER.toString(), "Something went" +
                    " wrong with the given input."); // TODO
            return;
        }

        flightService.cancelFlightBooking(bookFlightRequest.getTripId(), bookFlightRequest.getTravellerName());
        logger.info("Flight successfully cancelled which is associated with Trip: " + bookFlightRequest.getTripId());

        externalTaskService.complete(externalTask);
        logger.info("Finished Task: " + externalTask.getActivityId() + "(ID: " + externalTask.getId() + ")");
    }
}
