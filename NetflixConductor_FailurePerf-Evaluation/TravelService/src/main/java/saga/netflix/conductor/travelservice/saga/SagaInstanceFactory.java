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

    private final String conductorServerUri;

    public SagaInstanceFactory(final WorkflowClient workflowClient, final String conductorServerUri) {
        this.workflowClient = workflowClient;
        this.conductorServerUri = conductorServerUri;
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

        // provoke different failure scenarios
        String failureInput = bookTripSagaData.getTripInformation().getDestination().getCountry();
        provokeFailures(failureInput, bookTripSagaRequest);
    }

    private void provokeFailures(final String failureInput, final StartWorkflowRequest bookTripSagaRequest) {
        // provoke to start same Saga again
        provokeSagaStartAgain(failureInput, bookTripSagaRequest);

        // provoke to start same Saga again after 5 minutes
        provokeOldMessageToOrchestrator(failureInput, bookTripSagaRequest);
    }

    private void provokeSagaStartAgain(final String failureInput, final StartWorkflowRequest bookTripSagaRequest) {
        if (!failureInput.equalsIgnoreCase("Provoke duplicate Saga start")) {
            return;
        }

        logger.info("Provoking immediate duplicate Saga start");
        // TODO check meaningfulness because engine cannot necessarily know about duplicate here
        workflowClient.startWorkflow(bookTripSagaRequest);
    }

    private void provokeOldMessageToOrchestrator(final String failureInput,
                                                 final StartWorkflowRequest bookTripSagaRequest) {
        if (!failureInput.equalsIgnoreCase("Provoke sending old Saga start message")) {
            return;
        }

        logger.info("Provoking delayed duplicate Saga start --> old message");
        // TODO check meaningfulness because engine cannot necessarily know about duplicate here
        OldSagaMessageProvoker oldMessageProvoker = new OldSagaMessageProvoker(conductorServerUri, bookTripSagaRequest);
        new Thread(oldMessageProvoker).start();
    }
}
