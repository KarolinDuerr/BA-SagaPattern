package saga.camunda.travelservice.saga.adapters;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import saga.camunda.hotelservice.api.HotelServiceChannels;
import saga.camunda.hotelservice.api.dto.BookHotelRequest;

@Component
public class BookHotelAdapter implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(BookHotelAdapter.class);

    @Override
    public void execute(DelegateExecution executionContext) throws Exception {
        BookHotelRequest bookHotelRequest = (BookHotelRequest) executionContext.getVariable(HotelServiceChannels.DataInput.BOOK_HOTEL_DATA);

        // Send
    }
}
