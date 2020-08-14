package saga.eventuate.tram.travelservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import saga.eventuate.tram.travelservice.error.TravelException;
import saga.eventuate.tram.travelservice.model.RejectionReason;
import saga.eventuate.tram.travelservice.model.TripInformation;

import javax.transaction.Transactional;
import java.util.List;

@Qualifier("TravelService")
public interface ITravelService {

    @Transactional
    List<TripInformation> getTripsInformation();

    @Transactional
    TripInformation getTripInformation(final Long tripId) throws TravelException;

    @Transactional
    TripInformation bookTrip(final TripInformation tripInformation);

    @Transactional
    boolean cancelTrip(final Long tripId) throws TravelException;

    @Transactional
    void rejectTrip(final Long tripId, final RejectionReason rejectionReason);

    @Transactional
    void confirmTripBooking(final Long tripId, final long hotelId, final long flightId);

}
