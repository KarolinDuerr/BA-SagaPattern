package saga.microprofile.travelservice.controller;

import saga.microprofile.travelservice.error.TravelException;
import saga.microprofile.travelservice.model.RejectionReason;
import saga.microprofile.travelservice.model.TripInformation;

import java.util.List;

public interface ITravelService {

    List<TripInformation> getTripsInformation();

    TripInformation getTripInformation(final Long tripId) throws TravelException;

    TripInformation bookTrip(final TripInformation tripInformation);

    void rejectTrip(final Long tripId, final RejectionReason rejectionReason);

    void confirmTripBooking(final Long tripId, final long hotelId, final long flightId);
}
