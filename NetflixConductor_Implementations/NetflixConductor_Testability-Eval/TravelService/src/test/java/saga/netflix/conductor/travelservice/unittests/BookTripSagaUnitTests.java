package saga.netflix.conductor.travelservice.unittests;

import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import saga.netflix.conductor.hotelservice.api.HotelServiceTasks;
import saga.netflix.conductor.travelservice.error.TravelException;
import saga.netflix.conductor.travelservice.model.Location;
import saga.netflix.conductor.travelservice.model.TripDuration;
import saga.netflix.conductor.travelservice.model.TripInformation;
import saga.netflix.conductor.travelservice.saga.SagaInstanceFactory;
import saga.netflix.conductor.travelservice.saga.Sagas;
import saga.netflix.conductor.travelservice.saga.bookTripSaga.BookTripSagaData;
import saga.netlfix.conductor.flightservice.api.FlightServiceTasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * It can be tested if the method uses the correct version and workflow name but not the workflow itself.
 * Additionally, since Conductor does not provide equals method for its classes, it can also not be tested if the
 * needed input parameters are set for this Saga.
 */
@SpringBootTest
public class BookTripSagaUnitTests {

    private SagaInstanceFactory sagaInstanceFactory;

    private WorkflowClient mockedWorkflowClient;

    @Before
    public void setUp() {
        this.mockedWorkflowClient = Mockito.mock(WorkflowClient.class);
        this.sagaInstanceFactory = new SagaInstanceFactory(mockedWorkflowClient);
    }

    @Test
    public void startBookTripSagaShouldUseCorrectWorkflowNameAndInput() throws TravelException, ParseException {
        // setup
        BookTripSagaData bookTripSagaData = makeBookTripSagaData();
        StartWorkflowRequest bookTripSagaRequest = makeBookTripWorkflowRequest(bookTripSagaData);


        // execute
        sagaInstanceFactory.startBookTripSaga(bookTripSagaData);

        // verify
        /**
         * Conductor's {@link StartWorkflowRequest} class does not provide an equals method and includes two maps
         * and an instance of {@link com.netflix.conductor.common.metadata.workflow.WorkflowDef}. Therefore, using
         * Mockito.eq() to check if the startWorkflow() method is called with the same information is not possible.
         * However, in order to compare the methods which can be compared, Mockito.refEq() is used and the problematic
         * fields (workflowDef, taskDomain and input) are excluded.
         */
        Mockito.verify(mockedWorkflowClient, Mockito.times(1)).startWorkflow(Mockito.refEq(bookTripSagaRequest,
                "workflowDef", "taskToDomain", "input"));
    }

    private BookTripSagaData makeBookTripSagaData() throws ParseException, TravelException {
        TripDuration tripDuration =
                new TripDuration(Date.from(new SimpleDateFormat("dd-MM-yyyy").parse("01-12-2021").toInstant()),
                        Date.from(new SimpleDateFormat("dd-MM-yyyy").parse("12-12-2021").toInstant()));
        Location start = new Location("Germany", "Munich");
        Location destination = new Location("USA", "Tampa");
        TripInformation tripInformation = new TripInformation(tripDuration, start, destination, "Max Mustermann",
                "breakfast", 1);
        return new BookTripSagaData(1L, tripInformation);
    }

    private StartWorkflowRequest makeBookTripWorkflowRequest(final BookTripSagaData bookTripSagaData) {
        StartWorkflowRequest bookTripSagaRequest = new StartWorkflowRequest();
        bookTripSagaRequest.setVersion(1);
        bookTripSagaRequest.setCorrelationId(Long.toString(bookTripSagaData.getTripId()));
        bookTripSagaRequest.setName(Sagas.BOOK_TRIP_SAGA);

        final Map<String, Object> inputParameters = new HashMap<>();
        inputParameters.put(HotelServiceTasks.TaskInput.BOOK_HOTEL_INPUT, bookTripSagaData.makeBookHotelRequest());
        inputParameters.put(FlightServiceTasks.TaskInput.BOOK_FLIGHT_INPUT, bookTripSagaData.makeBookFlightRequest());
        bookTripSagaRequest.setInput(inputParameters);
        return bookTripSagaRequest;
    }
}
