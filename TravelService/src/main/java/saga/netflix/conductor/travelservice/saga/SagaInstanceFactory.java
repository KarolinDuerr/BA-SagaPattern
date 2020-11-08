package saga.netflix.conductor.travelservice.saga;

import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.travelservice.api.TravelServiceTasks;
import saga.netflix.conductor.travelservice.saga.bookTripSaga.BookTripSagaData;

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
        bookTripSagaRequest.setVersion(1);
        bookTripSagaRequest.setCorrelationId(Long.toString(bookTripSagaData.getTripId()));
        bookTripSagaRequest.setName(Sagas.BOOK_TRIP_SAGA);

        // input that's necessary for the individual tasks
        final Map<String, Object> inputParameters = new HashMap<>();
        inputParameters.put(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT, bookTripSagaData.makeBookHotelRequest());
        inputParameters.put(HotelServiceTasks.TaskInput.CANCEL_HOTEL_INPUT, bookTripSagaData.makeCancelHotelBooking());
        inputParameters.put(TravelServiceTasks.TaskInput.REJECT_TRIP_INPUT, bookTripSagaData.makeRejectTripBooking());

        bookTripSagaRequest.setInput(inputParameters);

        logger.info("Start BookTripSaga workflow");
        workflowClient.startWorkflow(bookTripSagaRequest);
    }
}
