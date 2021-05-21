package saga.camunda.travelservice.saga;

import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import saga.camunda.flightservice.api.FlightServiceTopics;
import saga.camunda.flightservice.api.dto.BookFlightRequest;
import saga.camunda.hotelservice.api.HotelServiceTopics;
import saga.camunda.hotelservice.api.dto.BookHotelRequest;
import saga.camunda.travelservice.api.TravelServiceTopics;

import java.util.HashMap;

public class BookTripSaga {

    @Autowired
    private final ProcessEngine camunda;

    public BookTripSaga(final ProcessEngine camunda) {
        this.camunda = camunda;
    }

    public void bookTrip(final BookTripSagaData bookTripSagaData) {
        BookHotelRequest bookHotelRequest = bookTripSagaData.makeBookHotelRequest();
        BookFlightRequest bookFlightRequest = bookTripSagaData.makeBookFlightRequest();

        HashMap<String, Object> processVariables = new HashMap<>();
        processVariables.put(HotelServiceTopics.DataInput.BOOK_HOTEL_DATA, bookHotelRequest);
        processVariables.put(FlightServiceTopics.DataInput.BOOK_FLIGHT_DATA, bookFlightRequest);
        processVariables.put(TravelServiceTopics.DataInput.BOOK_TRIP_ID, bookTripSagaData.getTripId());

        camunda.getRuntimeService().startProcessInstanceByKey("BookTripSaga", processVariables);
    }
}
