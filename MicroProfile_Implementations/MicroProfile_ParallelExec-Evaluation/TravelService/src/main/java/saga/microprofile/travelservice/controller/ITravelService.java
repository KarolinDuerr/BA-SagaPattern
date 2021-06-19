package saga.microprofile.travelservice.controller;

import saga.microprofile.travelservice.error.TravelException;
import saga.microprofile.travelservice.api.dto.RejectionReason;
import saga.microprofile.travelservice.model.TripInformation;
import saga.microprofile.travelservice.saga.BookTripSagaData;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;

public interface ITravelService {

    List<TripInformation> getTripsInformation();

    TripInformation getTripInformation(final Long tripId) throws TravelException;

    TripInformation bookTrip(final TripInformation tripInformation);

    void rejectTrip(final URI lraId);

    void confirmTripBooking(final Long tripId, final long hotelId, final long flightId);
}
