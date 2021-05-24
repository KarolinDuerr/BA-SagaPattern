package saga.camunda.travelservice.saga;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.beans.factory.annotation.Autowired;
import saga.camunda.customerservice.api.CustomerServiceTopics;
import saga.camunda.customerservice.api.dto.ValidateCustomerRequest;
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
        ValidateCustomerRequest customerId = new ValidateCustomerRequest(bookTripSagaData.getTripInformation().getCustomerId());

        // Use Spin's built-in JSON data format for deserialization
        ObjectValue typedBookHotelRequest =
                Variables.objectValue(bookHotelRequest).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();
        ObjectValue typedBookFlightRequest =
                Variables.objectValue(bookFlightRequest).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();
        ObjectValue typedCustomerId =
                Variables.objectValue(customerId).serializationDataFormat(Variables.SerializationDataFormats.JSON).create();

        HashMap<String, Object> processVariables = new HashMap<>();
        processVariables.put(HotelServiceTopics.DataInput.BOOK_HOTEL_DATA, typedBookHotelRequest);
        processVariables.put(FlightServiceTopics.DataInput.BOOK_FLIGHT_DATA, typedBookFlightRequest);
        processVariables.put(CustomerServiceTopics.DataInput.VALIDATE_CUSTOMER_DATA, typedCustomerId);
        processVariables.put(TravelServiceTopics.DataInput.BOOK_TRIP_ID, bookTripSagaData.getTripId());

        camunda.getRuntimeService().startProcessInstanceByKey("BookTripSaga", processVariables);
    }
}
