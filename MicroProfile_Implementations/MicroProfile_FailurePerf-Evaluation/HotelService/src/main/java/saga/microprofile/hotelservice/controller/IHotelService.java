package saga.microprofile.hotelservice.controller;

import saga.microprofile.hotelservice.error.HotelException;
import saga.microprofile.hotelservice.model.HotelBooking;
import saga.microprofile.hotelservice.model.HotelBookingInformation;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;

public interface IHotelService {

    List<HotelBooking> getHotelBookings();

    HotelBooking getHotelBooking(final Long bookingId) throws HotelException;

    HotelBooking bookHotel(final String travellerName, final HotelBookingInformation hotelBooking) throws HotelException;

    void cancelHotelBooking(final URI lraId);

    void confirmHotelBooking(final Long bookingId, final Long tripId);

    // provoke sending an old message to the orchestrator
    void provokeOldMessageToOrchestrator(final URI lraId);
}
