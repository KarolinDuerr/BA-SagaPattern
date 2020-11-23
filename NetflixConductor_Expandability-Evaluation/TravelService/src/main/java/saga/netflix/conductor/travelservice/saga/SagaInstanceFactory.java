package saga.netflix.conductor.travelservice.saga;

import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.customerservice.api.CustomerServiceTasks;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.travelservice.saga.bookTripSaga.BookTripSagaData;
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
        inputParameters.put(CustomerServiceTasks.TaskInput.VALIDATE_CUSTOMER_INPUT,
                bookTripSagaData.getTripInformation().getCustomerId());
        // the input for the bookHotel task: "bookHotelRequest"
        inputParameters.put(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT, bookTripSagaData.makeBookHotelRequest());
        // the input for the bookFlight task: "bookFlightRequest"
        inputParameters.put(FlightServiceTasks.TaskInput.BOOK_FLIGHT_INPUT, bookTripSagaData.makeBookFlightRequest());

        bookTripSagaRequest.setInput(inputParameters);

        logger.info("Start BookTripSaga workflow");
        // POST request to Conductor's "/workflow" endpoint
        workflowClient.startWorkflow(bookTripSagaRequest);
    }
}
