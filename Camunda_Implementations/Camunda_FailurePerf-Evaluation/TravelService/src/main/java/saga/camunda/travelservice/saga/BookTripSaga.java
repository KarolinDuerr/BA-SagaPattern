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
import saga.camunda.travelservice.saga.failure.OldSagaMessageProvoker;
import saga.camunda.travelservice.saga.failure.StartSagaRequest;

import java.util.HashMap;

public class BookTripSaga {

    private static final Logger logger = LoggerFactory.getLogger(BookTripSaga.class);

    @Autowired
    private final ProcessEngine camunda;

    private final String camundaServerUri;

    public BookTripSaga(final ProcessEngine camunda, final String camundaServerUri) {
        this.camunda = camunda;
        this.camundaServerUri = camundaServerUri;
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

        // provoke different failure scenarios
        String failureInput = bookTripSagaData.getTripInformation().getDestination().getCountry();
        provokeFailures(failureInput, processVariables);
    }

    private void provokeFailures(final String failureInput, final HashMap<String, Object> processVariables) {
        // provoke Orchestrator failure (Camunda Engine) before the Saga is being started
        // TODO check if meaningful test is possible
        provokeOrchestratorFailure(failureInput);

        // provoke to start same Saga again
        provokeSagaStartAgain(failureInput, processVariables);

        // provoke to start same Saga again after 5 minutes
        provokeOldMessageToOrchestrator(failureInput, processVariables);
    }

    private void provokeOrchestratorFailure(final String failureInput) {
        // provoke Orchestrator failure (Camunda Engine) before the Saga is being started
        if (failureInput.equalsIgnoreCase("Provoke orchestrator failure while starting trip booking")) {
            logger.info("Shutting down Camunda Engine due to corresponding input.");
            // TODO Provoke failure while trip being started --> like that?
            camunda.close();
        }
    }

    private void provokeSagaStartAgain(final String failureInput, final HashMap<String, Object> processVariables) {
        if (!failureInput.equalsIgnoreCase("Provoke duplicate Saga start")) {
            return;
        }

        logger.info("Provoking immediate duplicate Saga start");
        // TODO check meaningfulness because engine cannot necessarily know about duplicate here
        camunda.getRuntimeService().startProcessInstanceByKey("BookTripSaga", processVariables);
    }

    // TODO fix me --> HotelService cannot deserialze request
    private void provokeOldMessageToOrchestrator(final String failureInput, final HashMap<String, Object> processVariables) {
        if (!failureInput.equalsIgnoreCase("Provoke sending old Saga start message")) {
            return;
        }

        logger.info("Provoking delayed duplicate Saga start --> old message");
        StartSagaRequest startSagaRequest = new StartSagaRequest("BookTripSaga", processVariables);
        ObjectValue typedStartSagaRequest =
                Variables.objectValue(startSagaRequest).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();

        OldSagaMessageProvoker oldMessageProvoker = new OldSagaMessageProvoker(camundaServerUri, typedStartSagaRequest);
        new Thread(oldMessageProvoker).start();
    }
}
