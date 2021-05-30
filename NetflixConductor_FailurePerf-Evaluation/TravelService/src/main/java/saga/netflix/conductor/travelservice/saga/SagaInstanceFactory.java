package saga.netflix.conductor.travelservice.saga;

import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        // the input for the bookHotel task: "bookHotelRequest"
        inputParameters.put(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT, bookTripSagaData.makeBookHotelRequest());
        // the input for the bookFlight task: "bookFlightRequest"
        inputParameters.put(FlightServiceTasks.TaskInput.BOOK_FLIGHT_INPUT, bookTripSagaData.makeBookFlightRequest());

        bookTripSagaRequest.setInput(inputParameters);

        logger.info("Start BookTripSaga workflow");
        // POST request to Conductor's "/workflow" endpoint
        String workflowId = workflowClient.startWorkflow(bookTripSagaRequest);

        // provoke different failure scenarios
        String failureInput = bookTripSagaData.getTripInformation().getDestination().getCountry();
        provokeFailures(failureInput, bookTripSagaRequest, workflowId);
    }

    private void provokeFailures(final String failureInput, final StartWorkflowRequest bookTripSagaRequest, final String workflowId) {
        // provoke to start same Saga again
        provokeSagaStartAgain(failureInput, bookTripSagaRequest, workflowId);

        // provoke to start same Saga again after 5 minutes
        provokeOldMessageToOrchestrator(failureInput, bookTripSagaRequest, workflowId);
    }

    // TODO check
    private void provokeSagaStartAgain(final String failureInput, final StartWorkflowRequest bookTripSagaRequest, final String workflowId) {
        if (!failureInput.equalsIgnoreCase("Provoke duplicate Saga start")) {
            return;
        }

        logger.info("Correlation ID: " + bookTripSagaRequest.getCorrelationId());
        bookTripSagaRequest.setCorrelationId(workflowId);
        // TODO check if a POST request instead of Java client usage is needed
        workflowClient.startWorkflow(bookTripSagaRequest);
    }

    // TODO check
    private void provokeOldMessageToOrchestrator(final String failureInput, final StartWorkflowRequest bookTripSagaRequest, final String workflowId) {
        if (!failureInput.equalsIgnoreCase("Provoke sending old Saga start message")) {
            return;
        }

        logger.info("Correlation ID: " + bookTripSagaRequest.getCorrelationId());
        bookTripSagaRequest.setCorrelationId(workflowId);

        OldSagaMessageProvoker oldMessageProvoker = new OldSagaMessageProvoker(failureInput, bookTripSagaRequest);
        new Thread(oldMessageProvoker).start();
    }
}
