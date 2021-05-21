package saga.camunda.travelservice.saga.adapters;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import saga.camunda.flightservice.api.FlightServiceTopics;
import saga.camunda.flightservice.api.dto.BookFlightResponse;
import saga.camunda.hotelservice.api.HotelServiceTopics;
import saga.camunda.hotelservice.api.dto.BookHotelResponse;
import saga.camunda.travelservice.api.TravelServiceTopics;
import saga.camunda.travelservice.controller.ITravelService;

@Component
public class ConfirmTripAdapter implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmTripAdapter.class);

    @Autowired
    private ITravelService travelService;

    public ConfirmTripAdapter(final ITravelService travelService) {
        this.travelService = travelService;
    }

    @Override
    public void execute(DelegateExecution executionContext) throws Exception {
        Long tripId = (Long) executionContext.getVariable(TravelServiceTopics.DataInput.BOOK_TRIP_ID);
        final BookHotelResponse bookHotelResponse = (BookHotelResponse) executionContext.getVariable(HotelServiceTopics.DataOutput.BOOK_HOTEL_RESPONSE);
        final BookFlightResponse bookFlightResponse = (BookFlightResponse) executionContext.getVariable(FlightServiceTopics.DataOutput.BOOK_FLIGHT_RESPONSE);
        logger.info("Received ConfirmTripBooking for tripId = " + tripId);

        travelService.confirmTripBooking(tripId, bookHotelResponse.getBookingId(), bookFlightResponse.getFlightBookingId());

        logger.info("Successfully confirmed trip with tripId = " + tripId);
    }
}
