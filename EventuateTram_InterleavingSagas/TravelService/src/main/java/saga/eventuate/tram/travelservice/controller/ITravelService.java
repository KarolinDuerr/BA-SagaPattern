package saga.eventuate.tram.travelservice.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import saga.eventuate.tram.travelservice.api.dto.FailureType;
import saga.eventuate.tram.travelservice.error.TravelException;
import saga.eventuate.tram.travelservice.model.BookingStatus;
import saga.eventuate.tram.travelservice.model.RejectionReason;
import saga.eventuate.tram.travelservice.model.TripInformation;

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
    BookingStatus cancelTrip(final Long tripId, final Long customerId, final FailureType provokeFailureType) throws TravelException;

    @Transactional
    void rejectTrip(final Long tripId, final RejectionReason rejectionReason);

    @Transactional
    void rejectTripCancellation(final Long tripId, final RejectionReason rejectionReason);

    @Transactional
    void confirmTripBooking(final Long tripId, final long hotelId, final long flightId);

    @Transactional
    void confirmTripCancellation(final Long tripId);

}
