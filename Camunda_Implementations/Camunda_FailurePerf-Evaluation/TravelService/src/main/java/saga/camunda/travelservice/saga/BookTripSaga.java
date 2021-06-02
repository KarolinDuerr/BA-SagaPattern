package saga.camunda.travelservice.saga;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import saga.camunda.flightservice.api.FlightServiceTopics;
import saga.camunda.flightservice.api.dto.BookFlightRequest;
import saga.camunda.hotelservice.api.HotelServiceTopics;
import saga.camunda.hotelservice.api.dto.BookHotelRequest;
import saga.camunda.travelservice.api.TravelServiceTopics;

import java.util.HashMap;

public class BookTripSaga {

    private static final Logger logger = LoggerFactory.getLogger(BookTripSaga.class);

    @Autowired
    private final ProcessEngine camunda;

    public BookTripSaga(final ProcessEngine camunda) {
        this.camunda = camunda;
    }

    public void bookTrip(final BookTripSagaData bookTripSagaData) {
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        BookFlightRequest bookFlightRequest = bookTripSagaData.makeBookFlightRequest();

        // Use Spin's built-in JSON data format for deserialization
        ObjectValue typedBookHotelRequest =
                Variables.objectValue(bookHotelRequest).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();
        ObjectValue typedBookFlightRequest =
                Variables.objectValue(bookFlightRequest).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();

        HashMap<String, Object> processVariables = new HashMap<>();
        processVariables.put(HotelServiceTopics.DataInput.BOOK_HOTEL_DATA, typedBookHotelRequest);
        processVariables.put(FlightServiceTopics.DataInput.BOOK_FLIGHT_DATA, typedBookFlightRequest);
        processVariables.put(TravelServiceTopics.DataInput.BOOK_TRIP_ID, bookTripSagaData.getTripId());

        camunda.getRuntimeService().startProcessInstanceByKey("BookTripSaga", processVariables);

        // provoke Orchestrator failure (Camunda Engine) before the Saga is being started
        // TODO check if meaningful test is possible
        provokeOrchestratorFailure(bookTripSagaData.getTripInformation().getDestination().getCountry());
    }

    private void provokeOrchestratorFailure(final String failureInput) {
        // provoke Orchestrator failure (Camunda Engine) before the Saga is being started
        if (failureInput.equalsIgnoreCase("Provoke orchestrator failure while starting trip booking")) {
            logger.info("Shutting down Camunda Engine due to corresponding input.");
            // TODO Provoke failure while trip being started --> like that?
            camunda.close();
        }
    }

}
