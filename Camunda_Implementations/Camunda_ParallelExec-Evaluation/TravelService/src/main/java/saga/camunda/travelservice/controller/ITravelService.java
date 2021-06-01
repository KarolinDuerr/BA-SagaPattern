package saga.camunda.travelservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.camunda.travelservice.model.RejectionReason;
import saga.camunda.travelservice.model.TripInformation;
import saga.camunda.travelservice.error.TravelException;

import java.util.List;

@Qualifier("TravelService")
public interface ITravelService {

    List<TripInformation> getTripsInformation();

    TripInformation getTripInformation(final Long tripId) throws TravelException;

    TripInformation bookTrip(final TripInformation tripInformation);

    void rejectTrip(final Long tripId, final RejectionReason rejectionReason);

    void confirmTripBooking(final Long tripId, final long hotelId, final long flightId);
}
