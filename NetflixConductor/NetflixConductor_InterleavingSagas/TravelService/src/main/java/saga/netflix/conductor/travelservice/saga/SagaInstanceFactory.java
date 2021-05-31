package saga.netflix.conductor.travelservice.saga;

import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.travelservice.api.TravelServiceTasks;
import saga.netflix.conductor.travelservice.saga.bookTripSaga.BookTripSagaData;
import saga.netflix.conductor.travelservice.saga.cancelTripSaga.CancelTripSagaData;
import saga.netlfix.conductor.flightservice.api.FlightServiceTasks;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to start defined Saga workflows with its input.
 */
public class SagaInstanceFactory {

    private static final Logger logger = LoggerFactory.getLogger(SagaInstanceFactory.class);

    @Autowired
    private final WorkflowClient workflowClient;

    public SagaInstanceFactory(final WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    public void startBookTripSaga(BookTripSagaData bookTripSagaData) {
        logger.info("Start BookTripSaga with " + bookTripSagaData.toString());

        StartWorkflowRequest bookTripSagaRequest = new StartWorkflowRequest();
        // specify which workflow version should be started
        bookTripSagaRequest.setVersion(1);
        bookTripSagaRequest.setCorrelationId(Long.toString(bookTripSagaData.getTripId()));
        // the name of the workflow that should be started
        bookTripSagaRequest.setName(Sagas.BOOK_TRIP_SAGA);

        final Map<String, Object> inputParameters = new HashMap<>();
        // the input for the bookHotel task: "bookHotelRequest"
        inputParameters.put(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT, bookTripSagaData.makeBookHotelRequest());
        // the input for the bookFlight task: "bookFlightRequest"
        inputParameters.put(FlightServiceTasks.TaskInput.BOOK_FLIGHT_INPUT, bookTripSagaData.makeBookFlightRequest());

        bookTripSagaRequest.setInput(inputParameters);

        logger.info("Start BookTripSaga workflow");
        // POST request to Conductor's "/workflow" endpoint
        workflowClient.startWorkflow(bookTripSagaRequest);
    }

    public void startCancelTripSaga(CancelTripSagaData cancelTripSagaData) {
        logger.info("Start CancelTripSaga with " + cancelTripSagaData.toString());

        StartWorkflowRequest cancelTripSagaRequest = new StartWorkflowRequest();
        // specify which workflow version should be started
        cancelTripSagaRequest.setVersion(1);
        cancelTripSagaRequest.setCorrelationId(Long.toString(cancelTripSagaData.getTripId()));
        // the name of the workflow that should be started
        cancelTripSagaRequest.setName(Sagas.CANCEL_TRIP_SAGA);

        final Map<String, Object> inputParameters = new HashMap<>();
        // the input for the cancelHotel task: "cancelHotelRequest"
        inputParameters.put(HotelServiceTasks.TaskInput.CANCEL_HOTEL_INPUT, cancelTripSagaData.makeCancelHotelRequest());
        // the input for the cancelFlight task: "cancelFlightRequest"
        inputParameters.put(FlightServiceTasks.TaskInput.CANCEL_FLIGHT_INPUT, cancelTripSagaData.makeCancelFlightRequest());
        // the input for the confirmTripCancellation task: "tripId"
        inputParameters.put(TravelServiceTasks.TaskInput.CANCEL_TRIP_ID, cancelTripSagaData.getTripId());

        cancelTripSagaRequest.setInput(inputParameters);

        logger.info("Start CancelTripSaga workflow");
        // POST request to Conductor's "/workflow" endpoint
        workflowClient.startWorkflow(cancelTripSagaRequest);
    }
}
