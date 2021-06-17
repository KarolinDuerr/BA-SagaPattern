package saga.microprofile.travelservice.controller;

import saga.microprofile.travelservice.error.TravelException;
import saga.microprofile.travelservice.api.dto.RejectionReason;
import saga.microprofile.travelservice.model.TripInformation;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;

public interface ITravelService {

//    @Transactional
    List<TripInformation> getTripsInformation();

//    @Transactional
    TripInformation getTripInformation(final Long tripId) throws TravelException;

//    @Transactional
    TripInformation bookTrip(final TripInformation tripInformation);

//    @Transactional
    void rejectTrip(final URI lraId);

//    @Transactional
    void confirmTripBooking(final Long tripId, final long hotelId, final long flightId);
}
