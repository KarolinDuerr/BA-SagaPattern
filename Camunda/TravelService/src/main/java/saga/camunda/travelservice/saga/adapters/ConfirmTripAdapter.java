package saga.camunda.travelservice.saga.adapters;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import saga.camunda.flightservice.api.dto.BookFlightRequest;
import saga.camunda.hotelservice.api.HotelServiceChannels;

@Component
public class ConfirmTripAdapter implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmTripAdapter.class);

    @Override
    public void execute(DelegateExecution executionContext) throws Exception {
        BookFlightRequest bookFlightRequest = (BookFlightRequest) executionContext.getVariable(HotelServiceChannels.DataInput.BOOK_HOTEL_DATA);

        // Send
    }
}
