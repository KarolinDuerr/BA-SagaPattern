package saga.camunda.travelservice.saga.adapters;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import saga.camunda.flightservice.api.FlightServiceChannels;
import saga.camunda.flightservice.api.dto.BookFlightRequest;
import saga.camunda.hotelservice.api.HotelServiceChannels;
import saga.camunda.hotelservice.api.dto.BookHotelRequest;

@Component
public class BookFlightAdapter implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(BookFlightAdapter.class);

    @Override
    public void execute(DelegateExecution executionContext) throws Exception {
        BookFlightRequest bookFlightRequest = (BookFlightRequest) executionContext.getVariable(FlightServiceChannels.DataInput.BOOK_FLIGHT_DATA);

        // Send
    }
}
