package saga.eventuate.tram.travelservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.eventuate.tram.travelservice.error.TravelException;
import saga.eventuate.tram.travelservice.model.RejectionReason;
import saga.eventuate.tram.travelservice.model.TripInformation;

import java.util.List;

@Qualifier("TravelService")
public interface ITravelService {

    List<TripInformation> getTripsInformation();

    TripInformation getTripInformation(final Long tripId) throws TravelException;

    TripInformation bookTrip(final TripInformation tripInformation);

    boolean cancelTrip(final Long tripId) throws TravelException;

    void rejectTrip(final Long tripId, final RejectionReason rejectionReason);

    void confirmTripBooking(final Long tripId, final long hotelId, final long flightId);

}
